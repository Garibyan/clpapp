package com.clphub.clpapp.service;


import java.util.*;

import com.clphub.clpapp.model.Role;
import com.clphub.clpapp.model.User;
import com.clphub.clpapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public void saveUser(User user, String[] roles) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> rolesSet = new HashSet<Role>();
        for (String role : roles) {
            rolesSet.add(new Role(role));
        }
        user.setRoles(rolesSet);
        userRepository.save(user);
    }


    public List<User> getAllUsers() {
        List<User> result = (List<User>) userRepository.findAll();

        if (result.size() > 0) {
            return result;
        } else {
            return new ArrayList<User>();
        }
    }

    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    public User createOrUpdateUser(User user) {
        if (user.getId() == null) {
            user = userRepository.save(user);
            return user;
        } else {
            Optional<User> userUpdate = userRepository.findById(user.getId());

            if (userUpdate.isPresent()) {
                User newUser = userUpdate.get();
                newUser.setFirstName(user.getFirstName());
                newUser.setLastName(user.getLastName());
                newUser.setMiddleName(user.getMiddleName());
                newUser.setAddress(user.getAddress());
                newUser.setBirthDate(user.getBirthDate());
                newUser.setPassword(user.getPassword());
                newUser.setPid(user.getPid());
                newUser.setUsername(user.getUsername());

                newUser = userRepository.save(newUser);

                return newUser;
            } else {
                user = userRepository.save(user);
                return user;
            }
        }
    }

    public void deleteUSerById(Long id) {
        userRepository.deleteById(id);
    }
}