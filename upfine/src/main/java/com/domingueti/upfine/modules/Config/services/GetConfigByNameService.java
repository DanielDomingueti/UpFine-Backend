package com.domingueti.upfine.modules.Config.services;

import com.domingueti.upfine.modules.Config.dtos.ConfigDTO;
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
    public ConfigDTO execute(String name) {

        Optional<ConfigDTO> configDtoOptional = configRepository.findByNameAndDeletedAtIsNull(name);
//        if (!configDtoOptional.isPresent()) {
//            throw new NotFoundException("Config not found with name: " + name);
//        }
        return configDtoOptional.get();
    }

}
