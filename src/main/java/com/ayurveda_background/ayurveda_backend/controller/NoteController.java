package com.ayurveda_background.ayurveda_backend.controller;

import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.entity.Note;
import com.ayurveda_background.ayurveda_backend.sevice.AppUserService;
import com.ayurveda_background.ayurveda_backend.sevice.NoteService;
import com.ayurveda_background.ayurveda_backend.sevice.PlantService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PlantService plantService;

    @GetMapping
    public ResponseEntity<?>getAllNotesOfUser(){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        AppUser appUser= appUserService.findByUserName(username);
        List<Note> allNotes=appUser.getNotes();
        if(allNotes!=null){
        return new ResponseEntity<>(allNotes,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    //send a single note not an array of notes as controller expect user
    //to send a note at a time

    //No need of @Pathvariable String username

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note notes
                                          ){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        //System.out.println("Context holder username "+username);

        Note createdNote = noteService.saveNote(notes, username);

        if (createdNote != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping("{noteId}")
    public ResponseEntity<Note> getNotesById(@PathVariable String noteId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AppUser user = appUserService.findByUserName(username);
        //only logged-in user can get their notes
        List<Note> collectUserNote = user.getNotes().stream()
                .filter(note -> note.getId().toString().equals(noteId))
                .toList();

        Optional<Note> noteOptional = Optional.empty();
        if (!collectUserNote.isEmpty())
            noteOptional = noteService.getNotesById(noteId);

        return noteOptional.map(note -> new ResponseEntity<>(note, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable ObjectId noteId
                                       ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        noteService.deleteUserNote(noteId,username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable String noteId,
                                           @RequestBody Note updateNotes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AppUser appUser=appUserService.findByUserName(username);

        List<Note> collectUserNote = appUser.getNotes().stream()
                .filter(note -> note.getId().toString().equals(noteId))
                .toList();

        if(collectUserNote.isEmpty()){
            return new ResponseEntity<>("Note not found", HttpStatus.BAD_REQUEST);
        }

        Optional <Note> oldNote= noteService.getNotesById(noteId);
        if(oldNote.isEmpty()){
            return new ResponseEntity<>("Note not found in the database", HttpStatus.NOT_FOUND);
        }

        oldNote.ifPresent(note -> {
            if(updateNotes.getContent()!=null && !updateNotes.getContent().isEmpty()){
                note.setContent(updateNotes.getContent());
            }
            noteService.saveNote(note);
        });
        return ResponseEntity.status(HttpStatus.CREATED).body(oldNote);
        }
    }
