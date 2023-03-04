package com.domingueti.upfine.modules.User.repositories;

import com.domingueti.upfine.modules.User.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
