package com.GDP.TaskMasterDemo.Services;


import java.util.List;
import java.util.Map;

import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Model.User;

public interface TaskService {

    void createTask(Task task);

    void updateTask(Long id, Task task);

    void deleteTask(Long id);

    List<Task> findAll();

    List<Task> findByOwnerOrderByDateDesc(User user);

    void setTaskCompleted(Long id);

    void setTaskNotCompleted(Long id);

    List<Task> findFreeTasks();

    Task getTaskById(Long taskId);

    void assignTaskToUser(Task task, User user);

    void unassignTask(Task task);
    
    double getTaskCompletionPercentage(List<Task> tasks);
    
    double getAverageTaskCycleTime(List<Task> tasks) ;
    
    public List<Integer> getCompletionTask(List<Task> tasks);
    
    public List<Integer> getIncompleteTask(List<Task> tasks);
    
    public Map<String, Long> getAssigneeTaskCounts();
    
    public List<Task> getTasksForMonth(User user, int year, int month);

	public List<Task> findByName(String query);
	
	public void addComment(String content,User user,Task task);
}
