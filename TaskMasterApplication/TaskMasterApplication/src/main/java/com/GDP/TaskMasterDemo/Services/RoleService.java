package com.GDP.TaskMasterDemo.Services;

import org.springframework.beans.PropertyValues;

import com.GDP.TaskMasterDemo.Model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);

    List<Role> findAll();
}
