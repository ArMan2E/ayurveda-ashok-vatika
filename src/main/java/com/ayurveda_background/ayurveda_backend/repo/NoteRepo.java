package com.ayurveda_background.ayurveda_backend.repo;

import com.ayurveda_background.ayurveda_backend.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends MongoRepository<Note,String> {
}
