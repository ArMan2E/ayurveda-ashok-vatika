package com.ayurveda_background.ayurveda_backend.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.NonNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "AppUsers")

public class AppUser {

    @Id
    private String  id;
    //@Indexed(unique = true)
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @DBRef
    @JsonProperty("notes")
    private List<Note> notes=new ArrayList<>();

    @DBRef
    @JsonProperty("bookmarks")
    private List<Bookmark> bookmarks=new ArrayList<>();

    @JsonProperty("roles")
    private  String roles;

}
