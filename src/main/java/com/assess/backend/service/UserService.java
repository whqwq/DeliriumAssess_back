package com.assess.backend.service;

import com.assess.backend.entity.Administrator;
import com.assess.backend.entity.User;
import com.assess.backend.repository.AdministratorRepository;
import com.assess.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;

    @Autowired
    public UserService(UserRepository userRepository, AdministratorRepository administratorRepository) {
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
    }

    public ResponseEntity<APIResponse> getAllUsers(String phone) {
        Administrator administrator = administratorRepository.findByPhone(phone);
        if (administrator == null) {
            return ResponseEntity.ok(new APIResponse(1,"Not an admin user"));
        }
        Map<String, Object> data = Map.of("users", userRepository.findAllByDeletedFalse());
        return ResponseEntity.ok(new APIResponse(data));
    }

    public ResponseEntity<APIResponse> createUser(Map<String, Object> data) {
        String phone = data.get("phone").toString();
        if (userRepository.findByPhone(phone) != null) {
            // todo deleted user can be restored
            return ResponseEntity.ok(new APIResponse(1,"User already exists"));
        }
        User user = new User();
        user.setName(data.get("name").toString());
        user.setPhone(phone);
        user.setHospitalName(data.get("hospitalName").toString());
        user.setPassword("123456");
        user.setRemark(data.get("remark").toString());
        user.setCreateDate(LocalDate.now());
        userRepository.save(user);
        Map<String, Object> responseData = Map.of("user", user);
        return ResponseEntity.ok(new APIResponse(responseData));
    }

    public ResponseEntity<APIResponse> changePassword(Map<String, Object> data) {
        String phone = data.get("phone").toString();
        String password = data.get("password").toString();
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        Administrator administrator = administratorRepository.findByPhone(phone);
        if (user == null && administrator == null) {
            return ResponseEntity.ok(new APIResponse(1,"User or Admin not found"));
        }
        if (user != null) {
            if (!user.getPassword().equals(password)) {
                return ResponseEntity.ok(new APIResponse(2,"Password error"));
            }
            user.setPassword(data.get("newPassword").toString());
            userRepository.save(user);
            return ResponseEntity.ok(new APIResponse());
        }
        if (!administrator.getPassword().equals(password)) {
            return ResponseEntity.ok(new APIResponse(2,"Password error"));
        }
        administrator.setPassword(data.get("newPassword").toString());
        administratorRepository.save(administrator);
        return ResponseEntity.ok(new APIResponse());
    }

    public ResponseEntity<APIResponse> resetPassword(Map<String, Object> data) {
        String phone = data.get("phone").toString();
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(1,"User not found"));
        }
        user.setPassword("123456");
        userRepository.save(user);
        return ResponseEntity.ok(new APIResponse());
    }

    public ResponseEntity<APIResponse> deleteUser(Map<String, Object> data) {
        String phone = data.get("phone").toString();
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(1,"User not found"));
        }
        user.setDeleted(true);
        userRepository.save(user);
        return ResponseEntity.ok(new APIResponse());
    }

    public ResponseEntity<APIResponse> changeUserInfo(Map<String, Object> data) {
        String originalPhone = data.get("originalPhone").toString();
        User user = userRepository.findByPhoneAndDeletedFalse(originalPhone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(1,"User not found"));
        }
        user.setPhone(data.get("phone").toString());
        user.setName(data.get("name").toString());
        user.setRemark(data.get("remark").toString());
        user.setHospitalName(data.get("hospitalName").toString());
        userRepository.save(user);
        return ResponseEntity.ok(new APIResponse());
    }
}
