package ua.mike.sso.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.mike.sso.data.entities.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
}