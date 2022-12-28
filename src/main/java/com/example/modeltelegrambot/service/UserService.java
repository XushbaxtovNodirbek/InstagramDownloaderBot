package com.example.modeltelegrambot.service;

import com.example.modeltelegrambot.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(Long id);
    User getUser(Long id);
    List<User> getAll();
}
