package com.GDP.TaskMasterDemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Repository.TaskRepository;
import com.GDP.TaskMasterDemo.Services.TaskServiceImpl;

@SpringBootTest
public class TaskManagmentTest {
	@Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;
	
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testCreateTask_FR1_1() { // FR 1.1: Create tasks with valid input
    	Task task = new Task("Sample Task", "Description", LocalDateTime.now().plusDays(5), null, false, "Creator");

        when(taskRepository.getOne(task.getId())).thenReturn(task);
        
        taskService.setTaskCompleted(task.getId());

        assertTrue(task.isCompleted());
        assertNotNull(task.getCompleteDateTime());
        verify(taskRepository, times(1)).save(task);
    }
    
    @Test
    public void assignTaskToUser_assignsUserToTask() {
        Task task = new Task();
        User user = new User("test@mail.com", "Test","112233","images/teja.jpg");

        taskService.assignTaskToUser(task, user);

        assertEquals(user, task.getOwner());
        verify(taskRepository, times(1)).save(task);
    }
    
    @Test
    public void setTaskPriority_updatesTaskPriority() {
        Task task = new Task("Sample Task", "Description", LocalDateTime.now().plusDays(5), null, false, "Creator");
        
        task.setTaskPriority("HIGH");

        assertEquals("HIGH", task.getTaskPriority());
    }
    
    @Test
    public void getTasksForMonth_returnsTasksWithinMonth() {
        User user = new User("test@mail.com", "Test","112233","images/teja.jpg");
        LocalDateTime dateInMonth = LocalDateTime.of(2024, 11, 10, 0, 0);
        Task task = new Task("Monthly Task", "Description", dateInMonth, null, false, "Creator");

        when(taskRepository.findByOwnerOrderByDateDesc(user)).thenReturn(List.of(task));
        
        List<Task> tasks = taskService.getTasksForMonth(user, 2024, 11);

        assertEquals(1, tasks.size());
        assertEquals("Monthly Task", tasks.get(0).getName());
    }
    
    @Test
    public void setTaskDeadline_updatesTaskDeadline() {
        Task task = new Task("Sample Task", "Description", LocalDateTime.now().plusDays(5), null, false, "Creator");

        LocalDateTime newDeadline = LocalDateTime.now().plusDays(10);
        task.setDate(newDeadline);

        assertEquals(newDeadline, task.getDate());
    }
    
    @Test
    public void setTaskCompleted_marksTaskAsCompleted() {
        Task task = new Task("Sample Task", "Description", LocalDateTime.now().plusDays(5), null, false, "Creator");

        when(taskRepository.getOne(task.getId())).thenReturn(task);
        
        taskService.setTaskCompleted(task.getId());

        assertTrue(task.isCompleted());
        assertNotNull(task.getCompleteDateTime());
        verify(taskRepository, times(1)).save(task);
    }
   
}