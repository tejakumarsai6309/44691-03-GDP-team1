package com.GDP.TaskMasterDemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.GDP.TaskMasterDemo.Model.Role;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Model.UserActions;
import com.GDP.TaskMasterDemo.Repository.RoleRepository;
import com.GDP.TaskMasterDemo.Repository.UserActionsRepository;
import com.GDP.TaskMasterDemo.Repository.UserRepository;
import com.GDP.TaskMasterDemo.Services.UserServiceImpl;

@SpringBootTest
public class AuthenticationServiceTest {
	
	 private static final String ADMIN="ADMIN";
	 private static final String USER="USER";

	
	@Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;
    
    @Mock
    private UserActionsRepository userActionsRepository;

    private User testUser;


	@Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("test@mail.com", "TestUser", "encodedPassword", "images/test.jpg");
        testUser.setId(1L);

    }
	
	@Test
    public void testCreateUser() {
        User user = new User("test@mail.com", "Test","112233","images/teja.jpg");

        when(bCryptPasswordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);
        System.out.println("Test case for USer creation is Sucessfull");
        assertNotNull(createdUser);
    }
	
	@Test
	public void testChangeRoleToAdmin() {
	    User user = new User("test@mail.com", "Test", "112233", "images/teja.jpg");

        Role adminRole = roleRepository.findByRole(ADMIN);
//	    when(roleRepository.findByRole(ADMIN)).thenReturn(adminRole);

	    when(userRepository.save(user)).thenReturn(user);
	    
	    
	    User updatedUser = userService.changeRoleToAdmin(user);
	    List<UserActions> userActions = userActionsRepository.findByUserid(user.getId());
	    assertNotNull(updatedUser);  // Ensures the updatedUser is not null
        System.out.println("Test case for Change Role to Admin is Sucessfull");
	    assertEquals(adminRole, updatedUser.getRoles().get(0));
	}
	
	@Test
	public void testChangePassword() {
	    // Create a user and set an initial password
	    User user = new User("test@mail.com", "Test", "112233", "images/teja.jpg");
	    user.setPassword("oldEncodedPassword");

	    // Mock finding the user by email and encoding the new password
	    when(userRepository.findByEmail("test@mail.com")).thenReturn(user);
	    when(bCryptPasswordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

	    // Call changePassword
	    userService.changePassword("test@mail.com", "newPassword");
        System.out.println("Test case for Change Password is Sucessfull");
	    // Verify that the password was updated
	    assertEquals("encodedNewPassword", user.getPassword());
	}
	
//	@Test
//    public void testLogUserActionAfterRoleChange() {
//        // Setup role and action
//        Role adminRole = new Role();
//        adminRole.setRole("ADMIN");
//        
//        // Simulate user and role retrieval
//        when(roleRepository.findByRole("ADMIN")).thenReturn(adminRole);
//        when(userRepository.save(testUser)).thenReturn(testUser);
//
//        // Call the changeRoleToAdmin method
//        User updatedUser = userService.changeRoleToAdmin(testUser);
//
//        // Verify if the action was logged correctly
//        UserActions loggedAction = new UserActions(testUser.getId(), "Change Role", "User has changed Role to Admin");
//        verify(userActionsRepository, times(1)).save(loggedAction);
//
//        assertNotNull(updatedUser);
//        assertEquals(adminRole, updatedUser.getRoles().get(0));
//    }
//
//    @Test
//    public void testLogUserActionAfterPasswordReset() {
//        // Setup user retrieval and encoding password
//        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);
//        when(userRepository.save(testUser)).thenReturn(testUser);
//
//        // Call changePassword to reset user's password
//        userService.changePassword("test@mail.com", "newPassword");
//
//        // Verify if the action was logged correctly
//        UserActions loggedAction = new UserActions(testUser.getId(), "Password Reset", "User has reseted the password");
//        verify(userActionsRepository, times(1)).save(loggedAction);
//        
//        // Validate that the password has been updated
//        assertEquals("encodedPassword", testUser.getPassword());
//    }
	
}
