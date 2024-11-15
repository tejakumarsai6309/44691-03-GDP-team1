package com.GDP.TaskMasterDemo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GDP.TaskMasterDemo.Model.UserActions;

@Repository
public interface UserActionsRepository extends JpaRepository<UserActions, Long>{
	
	List<UserActions> findByUserid(Long userid);

}
