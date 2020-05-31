package me.decentos.service;

import me.decentos.model.User;

public interface UserService {
    User findByUsername(String username);
}
