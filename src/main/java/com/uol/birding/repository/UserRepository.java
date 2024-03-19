package com.uol.birding.repository;

import com.uol.birding.entity.User;
import com.uol.birding.enums.UserType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    List<User> findAllByUserType(UserType userType);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.locked=?2 WHERE u.email=?1 AND u.id>0", nativeQuery = true)
    public int updateUserLock(String email, Boolean locked);

    @Query(value = "SELECT CONCAT(user_type, ',', COUNT(id)) FROM birding.user GROUP BY user_type", nativeQuery = true)
    public List<String> getUserCountByUserType();

    public Long countUsersByUserType(UserType userType);
}
