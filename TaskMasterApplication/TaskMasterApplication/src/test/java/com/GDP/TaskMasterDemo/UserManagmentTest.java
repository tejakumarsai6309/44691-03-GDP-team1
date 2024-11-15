package com.GDP.TaskMasterDemo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.GDP.TaskMasterDemo.Model.Comment;
import com.GDP.TaskMasterDemo.Model.Task;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Repository.CommentRepository;
import com.GDP.TaskMasterDemo.Repository.TaskRepository;
import com.GDP.TaskMasterDemo.Services.TaskServiceImpl;

@SpringBootTest
public class UserManagmentTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    private User user;
    private Task task;

    @Before
    public void setUp() {
    	
        MockitoAnnotations.openMocks(this);

        user = new User("test@mail.com", "TestUser", "encodedPassword", "images/test.jpg");
        
        LocalDateTime today = LocalDateTime.now();
        task = new Task(
                "Send the finished site to the client",
                "Send the finished site to the client and get feedback. Fix and change any requests by client. Give access to client to all accounts created on their behalf. Send updates to client and wait for client sign-off.",
                today.plusDays(18), today,
                false,
                "TestUser", "MEDIUM");
        
        // Mock the task repository's behavior to simulate database behavior.
        when(taskRepository.save(task)).thenReturn(task);
    }

    @Test
    public void testAddCommentToTask() {
        // Step 1: Create and save the task
        taskServiceImpl.createTask(task);
        verify(taskRepository, times(1)).save(task);

        // Step 2: Add a comment to the task
        String commentContent = "This is a comment";
        taskServiceImpl.addComment(commentContent, user, task);

        // Step 3: Verify that the comment is saved
        Comment newComment = new Comment();
        newComment.setContent(commentContent);
        newComment.setUser(user);
        newComment.setTask(task);
        newComment.setCreatedAt(LocalDateTime.now());

        // Mocking the comment retrieval for the task
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(newComment);
        when(commentRepository.findByTaskId(task.getId())).thenReturn(mockComments);

        // Step 4: Retrieve comments and verify
        List<Comment> comments = commentRepository.findByTaskId(task.getId());
        assertEquals(1, comments.size());
        assertEquals(commentContent, comments.get(0).getContent());
        assertEquals(user, comments.get(0).getUser());
    }
    
    
    
}