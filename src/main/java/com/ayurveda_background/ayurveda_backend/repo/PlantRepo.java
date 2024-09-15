package com.ayurveda_background.ayurveda_backend.repo;

import com.ayurveda_background.ayurveda_backend.entity.Plant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepo extends MongoRepository<Plant, ObjectId> {
    Plant findById(String plantId);

    //@Query("{'commonName': ?0}")
    Optional<Plant> findByCommonNameIgnoreCase(String commonName);


    //UNIVERSAL SEARCH

    //$or means search either of them, $regex ?0 ?0 is referring to the value of the searchTerm parameter passed to the query method.
    //his is an option for the regular expression query that specifies case insensitivity.
    @Query("{'$or': [{'botanicalName': {$regex:?0,$options:'i'}},{'commonName':{$regex:?0, $options:'i'}},{'medicinalUses':{$regex:?0,$options:'i'}},{'cultivationMethods':{$regex:?0,$options:'i'}}]}")
    Optional<List<Plant>> searchAllFields(String searchTerm);
}
