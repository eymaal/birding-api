package com.uol.birding.repository;

import com.uol.birding.entity.Settings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SettingsRepository extends CrudRepository<Settings, String> {
    @Query(value = "SELECT s.value from Settings s where s.name = ?1", nativeQuery = true)
    public Boolean findValueByName(String name);
}
