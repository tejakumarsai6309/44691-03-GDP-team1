package com.GDP.TaskMasterDemo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Repository.TaskRepository;

@Service
public class DashboardService {
	
	@Autowired
    private TaskRepository taskRepository;


    // Save a new or updated task
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    // Fetch tasks for a specific user and sort them by date (most recent first)
    public List<Task> findByOwnerOrderByDateDesc(User user) {
        return taskRepository.findByOwnerOrderByDateDesc(user);
    }

    // Mark a task as completed
    public void setTaskCompleted(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(true);
            taskRepository.save(task);
        }
    }

    // Unmark a task as not completed
    public void setTaskNotCompleted(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(false);
            taskRepository.save(task);
        }
    }

    // Other business logic for tasks...
}
