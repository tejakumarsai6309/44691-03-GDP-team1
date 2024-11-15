package com.GDP.TaskMasterDemo.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.GDP.TaskMasterDemo.Model.Comment;
import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Model.UserActions;
import com.GDP.TaskMasterDemo.Repository.CommentRepository;
import com.GDP.TaskMasterDemo.Repository.UserActionsRepository;
import com.GDP.TaskMasterDemo.Services.TaskService;
import com.GDP.TaskMasterDemo.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;
    @Autowired
    private UserActionsRepository userActionsRepository;
    
    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String listTasks(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", false);
        return "views/tasks";
    }

    @GetMapping("/tasks/in-progress")
    public String listTasksInProgress(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", true);
        return "views/tasks";
    }

    private void prepareTasksListModel(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    @GetMapping("/task/create")
    public String showEmptyTaskForm(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        
        Task task = new Task();
        task.setCreatorName(user.getName());
        if (request.isUserInRole("ROLE_USER")) {
            task.setOwner(user);
        }
        model.addAttribute("task", task);
        
        return "forms/task-new";
    }

    @PostMapping("/task/create")
    public String createTask(@Valid Task task, BindingResult bindingResult, Principal principal) {
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
        if (bindingResult.hasErrors()) {
            return "forms/task-new";
        }
        taskService.createTask(task);
        UserActions userActions = new UserActions(user.getId(),"Create Task",task.getName()+" has been created by the user");
    	userActionsRepository.save(userActions);
        return "redirect:/tasks";
    }

    @GetMapping("/task/edit/{id}")
    public String showFilledTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "forms/task-edit";
    }

    @PostMapping("/task/edit/{id}")
    public String updateTask(@Valid Task task, BindingResult bindingResult, Principal principal, @PathVariable Long id, Model model) {
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
    	if (bindingResult.hasErrors()) {
            return "forms/task-edit";
        }
        taskService.updateTask(id, task);
        UserActions userActions = new UserActions(user.getId(),"Edited Task",task.getName()+" is edited by the "+user.getName());
    	userActionsRepository.save(userActions);
        return "redirect:/tasks";
    }
    
    @GetMapping("/task/{id}")
    public String showTask(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        model.addAttribute("comments", commentRepository.findByTaskId(id));
        return "forms/task-view";
    }

    @PostMapping("/task/{id}")
    public String viewTask(@Valid Task task, BindingResult bindingResult, Principal principal, @PathVariable Long id, Model model) {
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
    	if (bindingResult.hasErrors()) {
            return "forms/task-view";
        }
        taskService.updateTask(id, task);
        UserActions userActions = new UserActions(user.getId(),"Edited Task",task.getName()+" is edited by the "+user.getName());
    	userActionsRepository.save(userActions);
        return "redirect:/tasks";
    }

    @PostMapping("/task/{id}/addComment")
    public String addComment(@PathVariable Long id, @RequestParam("content") String content, Model model
    		, Principal principal) {
       

        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        Task task = taskService.getTaskById(id);
        if(user!=null) {
        	System.out.println("user exist");
        }
        if(task!=null) {
        	System.out.println("task exist");
        }
        taskService.addComment(content,user,task);
        return "redirect:/tasks";
    }

    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id, Principal principal) {
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
        UserActions userActions = new UserActions(user.getId(),"Deleted Task",taskService.getTaskById(id).getName()+" has been Deleted by the "+user.getName());
    	userActionsRepository.save(userActions);
        taskService.deleteTask(id);
        
        return "redirect:/tasks";
    }

    @GetMapping("/task/mark-done/{id}")
    public String setTaskCompleted(@PathVariable Long id, Principal principal) {
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
        UserActions userActions = new UserActions(user.getId(),"Marked Task",taskService.getTaskById(id).getName()+" has been Marked by the "+user.getName());
    	userActionsRepository.save(userActions);
        taskService.setTaskCompleted(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/unmark-done/{id}")
    public String setTaskNotCompleted(@PathVariable Long id, Principal principal) {
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
        UserActions userActions = new UserActions(user.getId(),"Unmarked Task",taskService.getTaskById(id).getName()+" is Unmarked by the "+user.getName());
    	userActionsRepository.save(userActions);
        taskService.setTaskNotCompleted(id);
        return "redirect:/tasks";
    }
    
    @GetMapping("/calendar/{year}/{month}")
    public String showCalendar(@PathVariable int year,
    		@PathVariable int month,Model model, Principal principal) throws JsonProcessingException {
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
        
        List<Task> tasks = taskService.getTasksForMonth(user,year, month);
        
        Map<Integer, String> taskByDate = new HashMap<>();

        for (Task task : tasks) {
            int date = task.getDate().getDayOfMonth(); // Get the day of the month from the task date
            taskByDate.put(date, task.getName()); // Add task name
        }
        System.out.println("### ==> taskByDate : "+taskByDate);
        
        ObjectMapper objectMapper = new ObjectMapper();
        String taskByDateJson = objectMapper.writeValueAsString(taskByDate);
        model.addAttribute("taskByDate", taskByDateJson);
        
        System.out.println("taskByDate : "+taskByDateJson);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
    	System.out.println("====>  Month : "+month+"  Year"+year);

        return "views/calendar"; // Return the calendar view
    }

    @GetMapping("/calendar")
    public String defaultCalendar(Model model, Principal principal) throws JsonProcessingException {
    	
    	String email = principal.getName();
        User user = userService.getUserByEmail(email);
        
        List<Task> tasks = taskService.getTasksForMonth(user,2024, 10);
                
        Map<Integer, String> taskByDate = new HashMap<>();

        for (Task task : tasks) {
            int date = task.getDate().getDayOfMonth(); // Get the day of the month from the task date
            taskByDate.put(date, task.getName()); // Add task name
        }
        System.out.println("### ==> taskByDate : "+taskByDate);
        
        ObjectMapper objectMapper = new ObjectMapper();
        String taskByDateJson = objectMapper.writeValueAsString(taskByDate);
        model.addAttribute("taskByDate", taskByDateJson);
        
        System.out.println("taskByDate : "+taskByDateJson);
        model.addAttribute("year", 2024);
        model.addAttribute("month", 10);
    	System.out.println("====>  Month : "+10+"  Year"+2024);

        return "views/calendar"; // Return the calendar view
    }
   
    @GetMapping("/tasks/search")
    public String tasksSearchList(@RequestParam("query") String query,Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
    	prepareSearchTasksList(query,model, principal, request);
        model.addAttribute("query", query);
        model.addAttribute("onlyInProgress", false);
        return "views/tasks";
    }
    
    
    private void prepareSearchTasksList(String query, Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");
    	List<Task> tasks = taskService.findByName(query);

        model.addAttribute("tasks", tasks);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    
    

}
