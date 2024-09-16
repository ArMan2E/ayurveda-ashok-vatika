package com.ayurveda_background.ayurveda_backend.sevice;

import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.entity.Bookmark;
import com.ayurveda_background.ayurveda_backend.entity.Plant;
import com.ayurveda_background.ayurveda_backend.repo.AppUserRepo;
import com.ayurveda_background.ayurveda_backend.repo.BookmarkRepo;
import com.ayurveda_background.ayurveda_backend.repo.PlantRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class BookmarkService {

    private static final Logger log = LoggerFactory.getLogger(BookmarkService.class);
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
        log.info("present user {}",username);
        if(user.isPresent()){
            List<Bookmark> oldbookmarks=user.get().getBookmarks();
            if(oldbookmarks!=null) {
                List<Bookmark> sameBookmark = oldbookmarks.stream()
                        .filter(x -> x.getPlant().getId().equals(bookmark.getPlant().getId()))
                        .toList();
                if (!sameBookmark.isEmpty()) {
                    return null;
                }
            }
            //Bookmark oldBookmark=bookmarkRepo.findById()
            //Plant newBookmarkPlant=plantRepo.findById(bookmark.getPlant().getId());
            bookmark.setDateAdded(LocalDateTime.now());
            Bookmark savedBookmark=bookmarkRepo.save(bookmark);
            AppUser newBookmarkUser=user.get();
            newBookmarkUser.getBookmarks().add(bookmark);
            appUserRepo.save(newBookmarkUser);
        }
        else {
            throw new UsernameNotFoundException("Username not found with username "+username);
        }
        return bookmark;
    }

    public void deleteUserBookmark(String  bookmarkId, String username) {
        AppUser appUser=appUserService.findByUserName(username);

        appUser.getBookmarks().removeIf(x-> x.getId().equals(bookmarkId));

        appUserService.insertUser(appUser);
        bookmarkRepo.deleteById(bookmarkId);

    }

    public Optional<Bookmark> getBookmarkById(ObjectId bookmarkId) {
        return bookmarkRepo.findById(bookmarkId);
    }
}
