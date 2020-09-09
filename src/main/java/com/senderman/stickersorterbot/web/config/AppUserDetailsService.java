package com.senderman.stickersorterbot.web.config;

import com.senderman.stickersorterbot.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findById(Integer.parseInt(username));
        if (user.isEmpty())
            throw new UsernameNotFoundException("No id " + username + " in database");

        var authorities = List.of(new SimpleGrantedAuthority("user"));
        return new User(username, user.get().getPassword(), authorities);
    }
}
