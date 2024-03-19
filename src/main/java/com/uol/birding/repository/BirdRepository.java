package com.uol.birding.repository;

import com.uol.birding.entity.Bird;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BirdRepository extends CrudRepository<Bird, Integer> {

    @Query("SELECT b FROM Bird b WHERE b.commonName=?1")
    public Optional<Bird> findByCommonName(String commonName);
}
