package com.domingueti.upfine.modules.Email.repositories;

import com.domingueti.upfine.modules.Email.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
