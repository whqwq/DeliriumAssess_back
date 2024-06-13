package com.assess.backend;

import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@RestController
@SpringBootApplication
public class BackendApplication {
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @RequestMapping("/")
    List<Map<String, Object>> home() {
        String sql="select * from assess.user";//SQL查询语句
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
