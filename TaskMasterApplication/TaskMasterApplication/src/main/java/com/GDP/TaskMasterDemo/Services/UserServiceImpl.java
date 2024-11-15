package com.GDP.TaskMasterDemo.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.GDP.TaskMasterDemo.Model.Role;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Model.UserActions;
import com.GDP.TaskMasterDemo.Repository.RoleRepository;
import com.GDP.TaskMasterDemo.Repository.TaskRepository;
import com.GDP.TaskMasterDemo.Repository.UserActionsRepository;
import com.GDP.TaskMasterDemo.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private static final String ADMIN="ADMIN";
    private static final String USER="USER";

    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserActionsRepository userActionsRepository;



    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TaskRepository taskRepository,
                           RoleRepository roleRepository,UserActionsRepository userActionsRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userActionsRepository = userActionsRepository;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        Role userRole = roleRepository.findByRole(USER);
//        user.setRoles(new ArrayList<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }
    
    @Override
    public User changeRoleToAdmin(User user) {
        Role adminRole = roleRepository.findByRole(ADMIN);
        user.setRoles(new ArrayList<>(Collections.singletonList(adminRole)));
        UserActions userActions = new UserActions(user.getId(),"Change Role","User has changed Role to Admin");
    	userActionsRepository.save(userActions);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isUserEmailPresent(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.getOne(id);
        user.getTasksOwned().forEach(task -> task.setOwner(null));
        userRepository.delete(user);
    }

    @Override
    public void changePassword(String email, String newPassword) {
    	User oldUser = getUserByEmail(email);
    	oldUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
    	userRepository.save(oldUser);
    	UserActions userActions = new UserActions(oldUser.getId(),"Password Reset","User has reseted the password");
    	userActionsRepository.save(userActions);
    }
    
    
}

