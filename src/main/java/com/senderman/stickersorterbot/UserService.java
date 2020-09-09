package com.senderman.stickersorterbot;

import com.senderman.stickersorterbot.model.User;
import com.senderman.stickersorterbot.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user with random UUID password
     *
     * @param userId id of new user
     * @return new User object
     */
    public User newUser(int userId) {
        var passwordUuid = UUID.randomUUID().toString();
        var encodedPassword = passwordEncoder.encode(passwordUuid);
        var user = new User(userId, encodedPassword);
        return repo.save(user);
    }

    /**
     * Check if user with given userId exists
     *
     * @param userId id of user
     * @return true if exists, else false
     */
    public boolean userExists(int userId) {
        return repo.existsById(userId);
    }

    /**
     * Changes user's password. Creates a new user if not exists
     *
     * @param userId   id of user
     * @param password raw password
     * @return updated user
     */
    public User changePassword(int userId, String password) {
        var user = repo.findById(userId).orElse(newUser(userId));
        user.setPassword(passwordEncoder.encode(password));
        return repo.save(user);
    }

    /**
     * Delete all user data from repo
     *
     * @param userId id of user
     */
    public void deleteUser(int userId) {
        repo.deleteById(userId);
    }

}
