package com.assess.backend.repository;

import com.assess.backend.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Administrator findById(long id);
    Administrator findByPhone(String phone);
    void deleteByPhone(String phone);
    Administrator save(Administrator administrator);
}
