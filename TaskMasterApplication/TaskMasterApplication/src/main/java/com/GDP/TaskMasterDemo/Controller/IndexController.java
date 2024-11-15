package com.GDP.TaskMasterDemo.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Services.TaskService;
import com.GDP.TaskMasterDemo.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
public class IndexController {
	
	@Autowired
	private UserService userService;
	@Autowired
    private TaskService taskService;

    @GetMapping("/index")
    String showIndex(Model model, Principal principal) throws JsonProcessingException {
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Task> tasks = taskService.findByOwnerOrderByDateDesc(user);
        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        long incompleteTasks = totalTasks - completedTasks;
        long overdueTasks = tasks.stream().filter(task -> task.getDate().isBefore(LocalDateTime.now()) && !task.isCompleted()).count();
        
        double taskCompletionPercentage = taskService.getTaskCompletionPercentage(tasks);
        double avgTaskCycleTime = taskService.getAverageTaskCycleTime(tasks);
        
        List<Integer> completeTasksByPriority = taskService.getCompletionTask(tasks);  
        List<Integer> incompleteTasksByPriority = taskService.getIncompleteTask(tasks);
        System.out.println("### ==> completeTasksByPriority : "+completeTasksByPriority);
        System.out.println("### ==> incompletedTasksByPriority : "+incompleteTasksByPriority);
        
        Map<String, Long> assigneeTaskCounts = taskService.getAssigneeTaskCounts();

        System.out.println("### ==> assigneeTaskCounts : "+assigneeTaskCounts);
        
        ObjectMapper objectMapper = new ObjectMapper();
        String assigneeTaskCountsJson = objectMapper.writeValueAsString(assigneeTaskCounts);
        model.addAttribute("assigneeTaskCounts", assigneeTaskCountsJson);

        
       
        // Add the data to the model
        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("incompleteTasks", incompleteTasks);
        model.addAttribute("overdueTasks", overdueTasks);
        model.addAttribute("taskCompletionPercentage", taskCompletionPercentage);
        model.addAttribute("avgTaskCycleTime", avgTaskCycleTime);
        model.addAttribute("completeTasksByPriority", completeTasksByPriority);
        model.addAttribute("incompleteTasksByPriority", incompleteTasksByPriority);
        //model.addAttribute("assigneeTaskCounts", assigneeTaskCounts);


        return "index";
    }
    
    

}
