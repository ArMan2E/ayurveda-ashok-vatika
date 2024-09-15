package com.ayurveda_background.ayurveda_backend.controller;

import com.ayurveda_background.ayurveda_backend.entity.Plant;
import com.ayurveda_background.ayurveda_backend.sevice.PlantService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/plants")
public class PlantController {

    private static final Logger log = LoggerFactory.getLogger(PlantController.class);
    @Autowired
    PlantService plantService;

    @PostMapping("/add")
    public ResponseEntity<Plant>  addPlant( @RequestBody Plant plants){
        log.info("plant data{} ",plants.toString());
//        Plant setPlant=new Plant();
//         setPlant = plantService.insertPlant(plants);
        Plant savedPlant=plantService.insertPlant(plants);
        return new ResponseEntity<>(savedPlant, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Plant>> getAllPlants(){
        List<Plant> allPlants =plantService.getAllPlants();
        return new ResponseEntity<>(allPlants,HttpStatus.OK);
    }
    @GetMapping("/{plantId}")
    public ResponseEntity<?> getPlantById(@PathVariable ObjectId plantId){

        Optional<Plant> plantA=plantService.findByAPlantId(plantId);
        if(plantA.isEmpty()){
            return new ResponseEntity<>("Plant NOT Present",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(plantA.orElseThrow(),HttpStatus.OK);
    }

    @GetMapping("/common/{commonName}")
    public ResponseEntity<?> getPlantByCommonName(@RequestParam String commonName){

        Optional<Plant> plantA=plantService.findByAPlantCommonName(commonName);
        if(plantA.isEmpty()){
            return new ResponseEntity<>("Plant NOT Present",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(plantA.orElseThrow(),HttpStatus.OK);
    }

    //Universal Search
    @GetMapping("/search")
    public ResponseEntity<?> searchPlant(@RequestParam String term){
        List<Plant> plants=plantService.searchPlants(term).orElseThrow();
        if(plants.isEmpty()){
            return new ResponseEntity<>("No plants found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(plants,HttpStatus.OK);
    }


    @PutMapping("/update/{plantId}")
    public ResponseEntity<?> updateAPlant(@RequestBody Plant plant,
                                          @PathVariable ObjectId plantId){
        Plant existingPlant=plantService.findByPlantId(plantId.toString());
        if(existingPlant!=null){
            if(plant.getCommonName()!=null && !plant.getCommonName().isEmpty()){
                existingPlant.setCommonName(plant.getCommonName());
            }
            if (plant.getBotanicalName() != null && !plant.getBotanicalName().isEmpty()) {
                existingPlant.setBotanicalName(plant.getBotanicalName());
            }

            if (plant.getMedicinalUses() != null && !plant.getMedicinalUses().isEmpty()) {
                existingPlant.setMedicinalUses(plant.getMedicinalUses());
            }

            if (plant.getCultivationMethods() != null && !plant.getCultivationMethods().isEmpty()) {
                existingPlant.setCultivationMethods(plant.getCultivationMethods());
            }

            if (plant.getImgUrl() != null && !plant.getImgUrl().isEmpty()) {
                existingPlant.setImgUrl(plant.getImgUrl());
            }

            if (plant.getVideosUrl() != null && !plant.getVideosUrl().isEmpty()) {
                existingPlant.setVideosUrl(plant.getVideosUrl());
            }

            if (plant.getAudioUrl() != null && !plant.getAudioUrl().isEmpty()) {
                existingPlant.setAudioUrl(plant.getAudioUrl());
            }

        Plant updatedPlant=plantService.insertPlant(existingPlant);

        return new ResponseEntity<>(existingPlant,HttpStatus.OK);
        }   else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{plantId}")
    public ResponseEntity<?> deletePlantById(@PathVariable ObjectId plantId){
        plantService.deletePlant(plantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Add Bookmarks and Note in the exposed endpoint

}
