package com.moondysmell.devstausers.controller;

import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.service.DevUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final DevUserService devUserService;

    @GetMapping("/getAll")
    public List<DevUser> getAllUsers() {
        return devUserService.findAll();
    }
    @GetMapping("/getByEmail")
    public DevUser getByEmail(@RequestParam String email) {
        return devUserService.findUserByEmail(email);

    }
}
