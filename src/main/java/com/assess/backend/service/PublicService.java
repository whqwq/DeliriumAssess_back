package com.assess.backend.service;

import com.assess.backend.entity.Administrator;
import com.assess.backend.entity.User;
import com.assess.backend.repository.AdministratorRepository;
import com.assess.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PublicService {
    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    @Autowired
    public PublicService(UserRepository userRepository, AdministratorRepository administratorRepository) {
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
    }
    public ResponseEntity<APIResponse> login(Map<String, Object> data) {
        String phone = data.get("phone").toString();
        String password = data.get("password").toString();
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        Administrator administrator = administratorRepository.findByPhone(phone);
        if (user == null && administrator == null) {
            return ResponseEntity.ok(new APIResponse(1,"User does not exist"));
        }
        if (user != null && !user.getPassword().equals(password) || administrator != null && !administrator.getPassword().equals(password)) {
            return ResponseEntity.ok(new APIResponse(2,"Password is incorrect"));
        }
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("phone", phone);
        responseData.put("name", user != null ? user.getName() : administrator.getName());
        responseData.put("id", user != null ? user.getId() : administrator.getId());
        responseData.put("isAdmin", administrator != null);
        responseData.put("token", "token"); //todo generate token
        return ResponseEntity.ok(new APIResponse(responseData));
    }
}
