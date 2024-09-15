package com.ayurveda_background.ayurveda_backend.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "plants")
@Data
public class Plant {
/**
 *
 * ALWAYS ADD @JsonProperty("<Property-Name>")
 * this will help to manage the json serialization and deserialization
 * over the internet
 * */
    @MongoId
    private ObjectId id;
    @JsonProperty("botanicalName")
    private String botanicalName;

    @JsonProperty("commonName")
    private String commonName;

   @JsonProperty("medicinalUses")
    private List<String> medicinalUses;

  @JsonProperty("cultivationMethods")
    private String cultivationMethods;

  @JsonProperty("imgUrl")
    private List<String> imgUrl;

  @JsonProperty("videosUrl")
    private List<String> videosUrl;

  @JsonProperty("audioUrl")
    private List<String>  audioUrl;
}
