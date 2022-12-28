package com.example.modeltelegrambot.service.impl;

import com.example.modeltelegrambot.entity.User;
import com.example.modeltelegrambot.repository.UserRepository;
import com.example.modeltelegrambot.service.UserService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceIml implements UserService {
    private final UserRepository userRepository;
    @Override
    public User saveUser(Long id) {
        User user=new User();
        user.setChatId(id);
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
