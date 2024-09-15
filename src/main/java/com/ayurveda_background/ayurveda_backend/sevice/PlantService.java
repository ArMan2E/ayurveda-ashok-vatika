package com.ayurveda_background.ayurveda_backend.sevice;

import com.ayurveda_background.ayurveda_backend.entity.Plant;
import com.ayurveda_background.ayurveda_backend.repo.PlantRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PlantService {

    @Autowired
    PlantRepo plantRepo;

    public Plant insertPlant(Plant plant) {
        return plantRepo.save(plant);
    }

    public List<Plant> getAllPlants() {
        return plantRepo.findAll();
    }

    public Plant findByPlantId(String plantId) {
        return plantRepo.findById(plantId);
    }

    public void deletePlant(ObjectId plantId) {
        //Plant findPlant=plantRepo.findById(plantId);
        plantRepo.deleteById(plantId);
    }

    public Optional<Plant> findByAPlantId(ObjectId plantId){

        return plantRepo.findById(plantId);
    }

    public Optional<Plant> findByAPlantCommonName(String commonName) {
        return plantRepo.findByCommonNameIgnoreCase(commonName);
    }

    public Optional<List<Plant>> searchPlants(String searchTerm){
        return plantRepo.searchAllFields(searchTerm);
    }


    public Optional<Plant> getPlantById(ObjectId plantId) {
        return plantRepo.findById(plantId);
    }
}
