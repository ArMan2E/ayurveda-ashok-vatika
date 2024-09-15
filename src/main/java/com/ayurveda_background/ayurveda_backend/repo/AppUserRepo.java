package com.ayurveda_background.ayurveda_backend.repo;

import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.entity.Bookmark;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AppUserRepo extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);

    //List<Bookmark> findByBookmark();
}
