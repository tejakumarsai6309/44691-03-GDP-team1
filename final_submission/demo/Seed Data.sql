-- Creating User Table
CREATE TABLE Users (
    UserID INTEGER PRIMARY KEY,
    Username VARCHAR(50),
    Email VARCHAR(100),
    Password VARCHAR(255),
    Role VARCHAR(100)
);

-- Sample data for Users table
INSERT INTO Users (UserID, Username, Email, Password, Role) VALUES 
(1, 'john_doe', 'john.doe@example.com', 'hashedpassword1', 'team leader'),
(2, 'jane_smith', 'jane.smith@example.com', 'hashedpassword2', 'team member'),
(3, 'bob_jones', 'bob.jones@example.com', 'hashedpassword3', 'team member');

-- Creating Task Table
CREATE TABLE Tasks (
    TaskID INTEGER PRIMARY KEY,
    Description TEXT,
    Priority VARCHAR(100),
    Deadline DATETIME,
    Status VARCHAR(100),
    AssignedTo INTEGER,
    FOREIGN KEY (AssignedTo) REFERENCES Users(UserID)
);

-- Sample data for Tasks table
INSERT INTO Tasks (TaskID, Description, Priority, Deadline, Status, AssignedTo) VALUES
(1, 'Complete project proposal', 'High', '2024-06-01 12:00:00', 'In Progress', 2),
(2, 'Review code changes', 'Medium', '2024-05-20 18:00:00', 'Pending', 2),
(3, 'Prepare presentation slides', 'Low', '2024-05-25 15:00:00', 'completed', 3);

-- Creating TaskAssignments Table
CREATE TABLE TaskAssignments (
    AssignmentID INTEGER PRIMARY KEY,
    TaskID INTEGER,
    UserID INTEGER,
    FOREIGN KEY (TaskID) REFERENCES Tasks(TaskID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
-- Sample data for TaskAssignments table
INSERT INTO TaskAssignments (AssignmentID, TaskID, UserID) VALUES
(1, 1, 2),
(2, 2, 2),
(3, 3, 3);


-- Creating Sessions Table
CREATE TABLE Sessions (
    SessionID INTEGER PRIMARY KEY,
    UserID INTEGER,
    Timestamp DATETIME,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Sample data for Sessions table
INSERT INTO Sessions (SessionID, UserID, Timestamp) VALUES
(1, 1, '2024-05-15 09:00:00'),
(2, 2, '2024-05-16 10:30:00'),
(3, 3, '2024-05-17 14:45:00');

-- Creating Logs Table
CREATE TABLE Logs (
    ID INTEGER PRIMARY KEY,
    UserID INTEGER,
    Action TEXT,
    Timestamp DATETIME,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Sample data for Logs table
INSERT INTO Logs (ID, UserID, Action, Timestamp) VALUES
(1, 1, 'Task created', '2024-05-15 09:15:00'),
(1, 1, 'Task assigned', '2024-05-15 09:120:00'),
(2, 2, 'Task viewed', '2024-05-16 11:00:00'),
(2, 2, 'Task inProgress', '2024-05-16 11:00:00'),
(3, 3, 'Task completed', '2024-05-17 15:00:00');

-- Creating Reminder Table
CREATE TABLE Reminder (
    rem_ID INTEGER PRIMARY KEY,
    Task_ID INTEGER,
    reminderTime TIMESTAMP,
    FOREIGN KEY (Task_ID) REFERENCES Tasks(TaskID)
);

-- Sample data for Reminder table
INSERT INTO Reminder (rem_ID, Task_ID, reminderTime) VALUES
(1, 1, '2024-05-30 10:00:00'),
(2, 2, '2024-05-18 09:00:00');

