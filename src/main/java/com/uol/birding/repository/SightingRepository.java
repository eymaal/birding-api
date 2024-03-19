package com.uol.birding.repository;

import com.uol.birding.entity.Sighting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SightingRepository extends CrudRepository<Sighting, Integer> {

    public List<Sighting> findAllByObserverId(String observerId);
    public void deleteAllByObserverId(String observerId);
    @Query(value = "SELECT CONCAT(bird_id,',', SUM(quantity)) AS bird_stat from sighting group by bird_id ORDER BY SUM(quantity) DESC LIMIT 3", nativeQuery = true)
    public List<String> getTop3SightedBirds();
    @Query(value = "SELECT CONCAT(observer_id, ',', count(id)) AS user_stat FROM sighting GROUP BY observer_id ORDER BY count(id) DESC LIMIT 3", nativeQuery = true)
    public List<String> getTop3Observers();
}
