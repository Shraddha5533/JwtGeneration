package com.loginsecurityexample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loginsecurityexample.entity.UserInfo;
import com.loginsecurityexample.repository.UserInfoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoServic implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByusername(username);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {
        Optional<UserInfo> optionalValue = repository.findByusername(userInfo.getUsername());
        if (optionalValue.isPresent()) {
            return "User already Registered";
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }

    public UserInfo createUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        return repository.save(userInfo);
    }

    public UserInfo getUserById(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<UserInfo> getAllUsers() {
        return repository.findAll();
    }

    public UserInfo updateUser(int id, UserInfo userInfo) {
        UserInfo existingUser = repository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(userInfo.getUsername());
            existingUser.setEmail(userInfo.getEmail());
            if (!existingUser.getPassword().equals(userInfo.getPassword())) {
                existingUser.setPassword(encoder.encode(userInfo.getPassword()));
            }
            return repository.save(existingUser);
        }
        return null;
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }
}
