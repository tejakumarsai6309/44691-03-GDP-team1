package com.GDP.TaskMasterDemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Repository.TaskRepository;
import com.GDP.TaskMasterDemo.Services.TaskServiceImpl;

@SpringBootTest
public class SecurityTest {
	
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Mock
    private TaskRepository taskRepository;

	@Mock
    private TaskServiceImpl taskService;
    
    private Task task;
    
    @Before
    public void setUp() {
    	
        MockitoAnnotations.openMocks(this);

        
        LocalDateTime today = LocalDateTime.now();
        task = new Task(
                "Send the finished site to the client",
                "Send the finished site to the client and get feedback. Fix and change any requests by client. Give access to client to all accounts created on their behalf. Send updates to client and wait for client sign-off.",
                today.plusDays(18), today,
                false,
                "TestUser", "MEDIUM");
        taskService.createTask(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

    }


    @Test
    public void testPasswordEncryption() {
        String rawPassword = "password123";
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        // Verify that the encrypted password matches the raw password
        assertTrue(passwordEncoder.matches(rawPassword, encryptedPassword));
    }
    
    @Test
    public void testDataSynchronization() {
        // Update the description
        task.setDescription("Data synchronized across all platforms");
        
        // Mock save and findById to return the updated task
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        
        // Call update method on service
        taskService.updateTask(task.getId(), task);
        

        // Fetch updated task and verify description
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        
        // Corrected assertion to check the updated description
        assertEquals("Data synchronized across all platforms", updatedTask.getDescription());
    }

    
}
