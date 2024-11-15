package com.GDP.TaskMasterDemo.Services;


import java.util.List;

import com.GDP.TaskMasterDemo.Model.User;

public interface UserService {
    User createUser(User user);

    User changeRoleToAdmin(User user);

    List<User> findAll();

    User getUserByEmail(String email);

    boolean isUserEmailPresent(String email);

    User getUserById(Long userId);

    void deleteUser(Long id);
    
    void changePassword(String email,String newPassword);
}
