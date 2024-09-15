package com.ayurveda_background.ayurveda_backend.repo;

import com.ayurveda_background.ayurveda_backend.entity.Bookmark;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepo extends MongoRepository<Bookmark, ObjectId> {
}
