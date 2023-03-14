package com.domingueti.upfine.modules.Config.services;

import com.domingueti.upfine.exceptions.NotFoundException;
import com.domingueti.upfine.modules.Config.daos.ConfigDAO;
import com.domingueti.upfine.modules.Config.repositories.ConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class GetConfigByNameService {

    private ConfigRepository configRepository;

    @Transactional(readOnly = true)
    public ConfigDAO execute(String name) {
        Optional<ConfigDAO> configDaoOptional = configRepository.findByName(name);
        if (!configDaoOptional.isPresent()) {
            throw new NotFoundException("Config not found with name: " + name);
        }
        return configDaoOptional.get();
    }

}
