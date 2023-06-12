package com.domingueti.upfine.modules.User.repositories;

import com.domingueti.upfine.modules.User.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "" +
        "SELECT * FROM tb_user WHERE active = true AND deleted_at IS NULL",
        nativeQuery = true)
    List<User> findByActiveIsTrueAndDeletedAtIsNull();

    @Query(value = "" +
        "SELECT u.* " +
        "FROM tb_user u " +
        "WHERE u.email = :email " +
        "AND u.active IS TRUE " +
        "AND u.deleted_at IS NULL",
        nativeQuery = true)
    Optional<User> findByEmail(String email);

}
