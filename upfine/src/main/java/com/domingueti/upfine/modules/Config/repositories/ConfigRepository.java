package com.domingueti.upfine.modules.Config.repositories;

import com.domingueti.upfine.modules.Config.daos.ConfigDAO;
import com.domingueti.upfine.modules.Config.models.Config;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<Config, Long> {

    Optional<ConfigDAO> findByName(String name);

}
