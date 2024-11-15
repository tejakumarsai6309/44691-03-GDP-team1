package com.GDP.TaskMasterDemo.Services;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GDP.TaskMasterDemo.Model.Comment;
import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Repository.CommentRepository;
import com.GDP.TaskMasterDemo.Repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    
    private CommentRepository commentRepository;
    
    public TaskServiceImpl() {
    	
    }

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void createTask(Task task) {
    	task.setCreationDateTime(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Override
    public void updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.getOne(id);
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setDate(updatedTask.getDate());
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByOwnerOrderByDateDesc(User user) {
        return taskRepository.findByOwnerOrderByDateDesc(user);
    }

    @Override
    public void setTaskCompleted(Long id) {
        Task task = taskRepository.getOne(id);
        task.setCompleted(true);
        task.setCompleteDateTime(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Override
    public void setTaskNotCompleted(Long id) {
        Task task = taskRepository.getOne(id);
        task.setCompleted(false);
        task.setCompleteDateTime(null);
        taskRepository.save(task);
    }

    @Override
    public List<Task> findFreeTasks() {
        List<Task> freeTasks = new ArrayList<>();
        for (Task task : taskRepository.findAll()) {
            if (task.getOwner() == null && !task.isCompleted()) {
                freeTasks.add(task);
            }
        }
        return freeTasks;
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public void assignTaskToUser(Task task, User user) {
        task.setOwner(user);
        taskRepository.save(task);
    }

    @Override
    public void unassignTask(Task task) {
        task.setOwner(null);
        taskRepository.save(task);
    }
    
    @Override
    public double getTaskCompletionPercentage(List<Task> tasks) {
        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        
        if (totalTasks == 0) {
            return 0; // Avoid division by zero
        }
        return (double) completedTasks / totalTasks * 100;
    }
    
    @Override
    public double getAverageTaskCycleTime(List<Task> tasks) {
        return tasks.stream()
                .filter(Task::isCompleted)
                .peek(task -> {
                    System.out.println("Task Date: " + task.getDate() + ", Current Time: " + LocalDateTime.now());
                })
                .mapToDouble(task -> Math.abs(ChronoUnit.HOURS.between(LocalDateTime.now(),task.getDate())))
                .average()
                .orElse(0);  
    }
    
    @Override
    public List<Integer> getCompletionTask(List<Task> tasks) {
        List<Task> completedTasks = tasks.stream()
                .filter(Task::isCompleted)
                .toList();
        
        int high = (int) completedTasks.stream()
                .filter(completedTask -> "HIGH".equals(completedTask.getTaskPriority()))
                .count();
        
        int medium = (int) completedTasks.stream()
                .filter(completedTask -> "MEDIUM".equals(completedTask.getTaskPriority()))
                .count();
        
        int low = (int) completedTasks.stream()
                .filter(completedTask -> "LOW".equals(completedTask.getTaskPriority()))
                .count();
        
        // Use Arrays.asList to return the values in a list
        return Arrays.asList(high, medium, low);
    }
    
    @Override
    public List<Integer> getIncompleteTask(List<Task> tasks){
    	List<Task> incompleteTasks = tasks.stream().filter(task -> !task.isCompleted()).toList();
    	int high = (int)incompleteTasks.stream()
    			.filter(incompleteTask -> "HIGH".equals(incompleteTask.getTaskPriority())).count();
    	int medium = (int)incompleteTasks.stream()
    			.filter(incompleteTask -> "MEDIUM".equals(incompleteTask.getTaskPriority())).count();
    	int low = (int)incompleteTasks.stream()
    			.filter(incompleteTask -> "LOW".equals(incompleteTask.getTaskPriority())).count();
    	return Arrays.asList(high,medium,low);
    }
    
    @Override
    public Map<String, Long> getAssigneeTaskCounts() {
        List<Object[]> assigneeCounts = taskRepository.countTasksByAssignee();
        Map<String, Long> assigneeTaskCounts = new HashMap<>();
        
        for (Object[] row : assigneeCounts) {
            String assignee = (String) row[0];
            Long taskCount = (Long) row[1];
            assigneeTaskCounts.put(assignee, taskCount);
        }
        
        return assigneeTaskCounts;
    }
    
    @Override
    public List<Task> getTasksForMonth(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        List<Task> tasks = findByOwnerOrderByDateDesc(user);

        List<Task> tasksList = tasks.stream().filter(task -> task.getDate().isBefore(end) && task.getDate().isAfter(start)).toList();

    
       // return taskRepository.findByDateBetween(start, end);
        
        return tasksList;
    }
    public List<Task> findByName(String query){
		return taskRepository.findByNameContainingIgnoreCase(query);
	}
    
    public void addComment(String content,User user,Task task) {
    	Comment newComment = new Comment();
        newComment.setContent(content);
        newComment.setUser(user);
        newComment.setTask(task);
        newComment.setCreatedAt(LocalDateTime.now());
        System.out.println(newComment.toString());

        commentRepository.save(newComment);
        commentRepository.flush();
        
        task.addComment(newComment);
        
    }

}
