package com.ayurveda_background.ayurveda_backend.controller;

import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.entity.Bookmark;
import com.ayurveda_background.ayurveda_backend.repo.BookmarkRepo;
import com.ayurveda_background.ayurveda_backend.sevice.AppUserService;
import com.ayurveda_background.ayurveda_backend.sevice.BookmarkService;
import com.ayurveda_background.ayurveda_backend.sevice.PlantService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/bookmark")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private PlantService plantService;

    @Autowired
    private AppUserService appUserService;

    @GetMapping
    public ResponseEntity<?> getAllBookmarkOfUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        AppUser appUser=appUserService.findByUserName(username);
        List<Bookmark> allBookmarks=appUser.getBookmarks();
        if(allBookmarks!=null){
            return new ResponseEntity<>(allBookmarks, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

@GetMapping("/{bookmarkId}")
    public ResponseEntity<?> getBookmarkById(@PathVariable ObjectId bookmarkId){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();

        AppUser user=appUserService.findByUserName(username);

        List<Bookmark> collectUserBookmark=user.getBookmarks().stream()
                .filter(bookmark -> bookmark.getId().equals(bookmarkId))
                .toList();

        Optional<Bookmark> bookmarkOptional=Optional.empty();
        if(!collectUserBookmark.isEmpty()){
            bookmarkOptional=bookmarkService.getBookmarkById(bookmarkId);
        }
        return bookmarkOptional.map(bookmark -> new ResponseEntity<>(bookmark,HttpStatus.OK))
                .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @PostMapping
    public ResponseEntity<Bookmark> createBookmark(@RequestBody Bookmark bookmark){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();

        Bookmark createdBookmark=bookmarkService.saveBookmark(bookmark,username);

        if(createdBookmark!=null){
            return new ResponseEntity<>(bookmark,HttpStatus.CREATED);
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{bookmarkId}")
    public ResponseEntity<?> deleteBookmark(@PathVariable ObjectId bookmarkId){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();

        bookmarkService.deleteUserBookmark(bookmarkId,username);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}
