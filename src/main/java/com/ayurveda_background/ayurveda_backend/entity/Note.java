package com.ayurveda_background.ayurveda_backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "note")
public class Note {
        @Id
        private String id;

        @DBRef
        private Plant plant;

        @JsonProperty("title")
        private String title;

        @JsonProperty("content")
        private String content;

        @JsonProperty("createdAt")
        //@DateTimeFormat
        private LocalDateTime createdAt;
        //=LocalDateTime.now();  // Timestamp of when the note was created

    }
