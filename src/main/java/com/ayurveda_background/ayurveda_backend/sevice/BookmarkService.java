package com.ayurveda_background.ayurveda_backend.sevice;

import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.entity.Bookmark;
import com.ayurveda_background.ayurveda_backend.entity.Plant;
import com.ayurveda_background.ayurveda_backend.repo.AppUserRepo;
import com.ayurveda_background.ayurveda_backend.repo.BookmarkRepo;
import com.ayurveda_background.ayurveda_backend.repo.PlantRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepo bookmarkRepo;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private PlantRepo plantRepo;


    @Transactional
    public Bookmark saveBookmark(Bookmark bookmark, String username) {
        Optional<AppUser> user=appUserRepo.findByUsername(username);
        if(user.isPresent()){
            Plant newBookmarkPlant=plantRepo.findById(bookmark.getPlant().getId()).orElseThrow();
            bookmark.setDateAdded(LocalDateTime.now());
            Bookmark savedBookmark=bookmarkRepo.insert(bookmark);
            AppUser newBookmarkUser=user.get();
            newBookmarkUser.getBookmarks().add(bookmark);
            appUserRepo.save(newBookmarkUser);
        }
        else {
            throw new UsernameNotFoundException("Username not found with username "+username);
        }
        return bookmark;
    }

    public void deleteUserBookmark(ObjectId bookmarkId, String username) {
        AppUser appUser=appUserService.findByUserName(username);

        appUser.getBookmarks().removeIf(x->x.getId().equals(bookmarkId));

        appUserService.insertUser(appUser);
        bookmarkRepo.deleteById(bookmarkId);

    }

    public Optional<Bookmark> getBookmarkById(ObjectId bookmarkId) {
        return bookmarkRepo.findById(bookmarkId);
    }
}
