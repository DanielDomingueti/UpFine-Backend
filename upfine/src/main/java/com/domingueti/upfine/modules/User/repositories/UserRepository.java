package com.domingueti.upfine.modules.User.repositories;

import com.domingueti.upfine.modules.User.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "" +
        "SELECT email FROM tb_user WHERE active = true AND deleted_at IS NULL",
        nativeQuery = true)
    List<String> findEmailByActiveIsTrueAndDeletedAtIsNull();

    @Query(value = "" +
        "SELECT u.* " +
        "FROM tb_user u " +
        "WHERE u.email = :email " +
        "AND u.active IS TRUE " +
        "AND u.deleted_at IS NULL",
        nativeQuery = true)
    User findUserByEmail(String email);

    boolean existsByEmail(String email);
}
