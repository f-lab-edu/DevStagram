package com.moondysmell.devstausers.controller;

import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.repository.DevUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@AllArgsConstructor
@RestController
//@RequestMapping("/user")
public class UserController {
    private final DevUserRepository devUserRepository;

    @GetMapping("/getAll")
    public List<DevUser> getAllUsers() {
        return devUserRepository.findAll();
    }
}
