package com.uol.birding.repository;

import com.uol.birding.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findByOrderByDateTimeDesc();
    void deleteAllByAuthor(String author);
}
