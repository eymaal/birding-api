package com.uol.birding.repository;

import com.uol.birding.entity.UnmoderatedSighting;
import org.springframework.data.repository.CrudRepository;

public interface UnmoderatedSightingRepository extends CrudRepository<UnmoderatedSighting, Integer> {

    public void deleteAllByObserverId(String observerId);
}
