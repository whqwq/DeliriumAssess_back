package com.assess.backend.repository;

import com.assess.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    List<User> findAllByDeletedFalse();
    User findById(long id);
    User findByPhone(String phone);
    User findByPhoneAndDeletedFalse(String phone);
    void deleteByPhone(String phone);
    User save(User user);
}
