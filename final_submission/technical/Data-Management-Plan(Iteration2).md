### Data Management Plan for Task Management

### Users Table:
| Field    | Data Type           | Description                           |
|----------|---------------------|---------------------------------------|
| User ID  | INTEGER (Primary Key) | Unique identifier for each user.    |
| Username | VARCHAR(50)         | User's chosen username.               |
| Email    | VARCHAR(100)        | User's email address.                 |
| Password | VARCHAR(255) (encrypted) | Encrypted password for user authentication. |
| Role     | VARCHAR(100) | Role of the user (team leader or team member). |

### Tasks Table:
| Field        | Data Type          | Description                               |
|--------------|--------------------|-------------------------------------------|
| Task ID      | INTEGER (Primary Key) | Unique identifier for each task.       |
| Description  | TEXT               | Description of the task.                  |
| Priority     | VARCHAR(100)  | Priority level of the task.('Low', 'Medium', 'High')     |
| Deadline     | DATETIME           | Deadline for completing the task.         |
| Status       | VARCHAR(100)  | Current status of the task. ('Pending', 'In Progress', 'Completed')|
| Assigned To  | INTEGER (Foreign Key to Users) | User ID of the user to whom the task is assigned. |

### Task Assignments Table:
| Field          | Data Type           | Description                                 |
|----------------|---------------------|---------------------------------------------|
| Assignment ID  | INTEGER (Primary Key) | Unique identifier for each task assignment. |
| Task ID        | INTEGER (Foreign Key to Tasks) | Task ID of the assigned task.         |
| User ID        | INTEGER (Foreign Key to Users) | User ID of the user assigned to the task. |

### Sessions Table:
| Field       | Data Type           | Description                             |
|-------------|---------------------|-----------------------------------------|
| Session ID  | INTEGER (Primary Key) | Unique identifier for each session.  |
| User ID     | INTEGER (Foreign Key to Users) | User ID of the user associated with the session. |
| Timestamp   | DATETIME            | Timestamp indicating when the session occurred. |

### Logs Table:
| Field     | Data Type           | Description                                       |
|-----------|---------------------|---------------------------------------------------|
| ID    | INTEGER (Primary Key) | Unique identifier for each log entry.         |
| User ID   | INTEGER (Foreign Key to Users) | User ID of the user associated with the log entry. |
| Action    | TEXT                | Description of the action performed (e.g., task creation, assignment). |
| Timestamp | DATETIME            | Timestamp indicating when the action occurred.     |

### Reminder Table:
| Field        | Data Type               | Description                              |
|--------------|-------------------------|------------------------------------------|
| rem_ID           | INTEGER (Primary Key)    | Unique identifier for each reminder.     |
| Task_ID       | INTEGER (Foreign Key)    | Reference to the associated task.        |
| reminderTime | TIMESTAMP               | Date and time when the reminder is scheduled. |

### Relationships Between Tables:

1. **Users Table**:
   - One-to-Many Relationship with **Tasks Table**: One user can have many tasks assigned to them.
   - One-to-Many Relationship with **Task Assignments Table**: One user can be assigned to many tasks.
   - One-to-Many Relationship with **Sessions Table**: One user can have multiple sessions.

2. **Tasks Table**:
   - One-to-Many Relationship with **Task Assignments Table**: One task can have multiple assignments.

3. **Task Assignments Table**:
   - Many-to-One Relationship with **Users Table**: Many task assignments can be associated with one user.
   - Many-to-One Relationship with **Tasks Table**: Many task assignments can be associated with one task.

4. **Sessions Table**:
   - Many-to-One Relationship with **Users Table**: Many sessions can be associated with one user.

5. **Logs Table**:
   - Many-to-One Relationship with **Users Table**: Many logs can be associated with one user.

5. **Reminder Table**:
   - One-to-one Relationship with **Tasks Table**: one reminder can be associated with one task.

### Initial Plans to Secure Data:

1. **Access Restriction:**
   - Implement role-based access control (RBAC) to restrict access to certain features or data based on user roles. For example, team leaders might have access to additional administrative functions compared to team members.
   - Ensure that sensitive operations, such as task assignment or user management, are only accessible to authorized team Leaders.

2. **Encryption:**
   - Utilize strong encryption algorithms (e.g., 128-bit AES) to encrypt sensitive data such as passwords before storing them in the database.
   - Apply encryption techniques to protect communication between the client and server, especially when transmitting sensitive information like login credentials or task details.

3. **Session Management:**
   - Implement secure session management techniques to prevent session hijacking and unauthorized access.
   - Utilize techniques such as session tokens, HTTPS protocol, and secure cookies to ensure the integrity and confidentiality of user sessions.
   - Implement session timeout mechanisms to automatically log out users after a period of inactivity, reducing the risk of unauthorized access.

4. **Data Integrity:**
   - Implement measures to maintain data integrity, such as data validation and sanitization, to prevent injection attacks (e.g., SQL injection, XSS).
   - Regularly monitor and audit access logs to detect and respond to any unauthorized access attempts or suspicious activities.

### Mapping of Functional Requirements to Data Storage:

1. **Task Management:**
   - **Tasks, Task Assignments:** Store task data including descriptions, priorities, deadlines, and status in the Tasks table. Task assignments will link tasks to users, facilitating assignment and tracking.
   - **Users:** Store user data including usernames and roles to manage task assignment permissions.
   
2. **Collaboration:**
   - **User Data:** User data will include information about team members, enabling collaboration features such as task assignment and team communication.
   - **Task Assignments:** Task assignments will link tasks to users, facilitating collaboration by assigning tasks to team members.

3. **Security:**
   - **User Authentication Data:** Store user authentication data (e.g., usernames, encrypted passwords) in the Users table to authenticate users securely.
   - **Session Logs:** Store session logs containing user activity information (e.g., login/logout timestamps, actions performed) for security auditing purposes.

## ER Diagram

![ER Diagram](https://github.com/tejakumarsai6309/44691-03-GDP-team1/blob/main/ER%20Diagram.png)
