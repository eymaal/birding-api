package com.uol.birding.repository;

import com.uol.birding.entity.ImageFile;
import org.springframework.data.repository.CrudRepository;


public interface ImageFileRepository extends CrudRepository<ImageFile, Integer> {
}
