package com.GDP.TaskMasterDemo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GDP.TaskMasterDemo.Model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	 List<Comment> findByTaskId(Long taskId);

}
