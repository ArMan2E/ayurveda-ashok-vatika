package com.ayurveda_background.ayurveda_backend.sevice;

import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.entity.Note;
import com.ayurveda_background.ayurveda_backend.entity.Plant;
import com.ayurveda_background.ayurveda_backend.repo.AppUserRepo;
import com.ayurveda_background.ayurveda_backend.repo.NoteRepo;
import com.ayurveda_background.ayurveda_backend.repo.PlantRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private PlantRepo plantRepo;


    //transactional to save iff all operations are fulfilled
    @Transactional
    public Note saveNote(Note note, String username) {

        Optional<AppUser> user = appUserRepo.findByUsername(username);

        if (user.isPresent()) {          //type void
            //updating NOT inserting

           Plant newNotePlant=plantRepo.findById(note.getPlant().getId()).orElseThrow();
            // Set creation timestamp
            note.setCreatedAt(LocalDateTime.now());
            // Save the note to the repository
            Note savedNote = noteRepo.insert(note);
           AppUser newNoteUser=user.get();
            newNoteUser.getNotes().add(savedNote);
           appUserRepo.save(newNoteUser);


        }else {
            throw new UsernameNotFoundException("User not found with username "+username);

        }
        return note;
    }

    public void saveNote(Note note){
        //just saving it to noteRepo
        noteRepo.save(note);
    }
    public List<Note> getAllNotes(){
        return noteRepo.findAll();
    }


    public void deleteUserNote(ObjectId noteId, String userName) {
        AppUser appUser=appUserService.findByUserName(userName);
        //delete the id
        appUser.getNotes().removeIf(x->x.getId().equals(noteId));

        //need to update the notes after deletion
        appUserService.insertUser(appUser);
        //actually object id might cause error but working in bookmark service
        noteRepo.deleteById(String.valueOf(noteId));
    }

    public Optional<Note> getNotesById(String noteId) {
        return noteRepo.findById(noteId);
    }
}

