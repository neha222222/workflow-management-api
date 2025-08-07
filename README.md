# Workforce Management API

A comprehensive Spring Boot REST API for managing workforce tasks, assignments, and tracking. This application provides functionality for creating, assigning, and monitoring tasks for employees with advanced features like priority management, task comments, and activity history.

## Features

### Core Functionality
- ✅ Task creation, assignment, and management
- ✅ Staff management and task assignments
- ✅ Task status tracking (ACTIVE, COMPLETED, CANCELLED)
- ✅ Priority management (HIGH, MEDIUM, LOW)
- ✅ Task comments and activity history
- ✅ Smart daily task view with date range filtering

### Bug Fixes Implemented
- ✅ **Bug Fix 1**: Task re-assignment no longer creates duplicates - old tasks are properly marked as CANCELLED
- ✅ **Bug Fix 2**: Cancelled tasks are excluded from date range queries for cleaner views

### Advanced Features
- ✅ **Smart Daily View**: Shows active tasks from date range PLUS active tasks that started earlier but are still open
- ✅ **Priority Management**: Full priority system with update capabilities
- ✅ **Activity History**: Automatic logging of all task operations (creation, updates, reassignments)
- ✅ **Task Comments**: User comments with timestamps and authorship

## Technology Stack

- **Language**: Java 17+
- **Framework**: Spring Boot 3.0.4
- **Build Tool**: Gradle
- **Database**: In-memory collections (ConcurrentHashMap)
- **Dependencies**: Spring Web, Lombok

## Project Structure

```
src/main/java/com/workforcemgmt/
├── WorkforcemgmtApplication.java    
├── controller/
│   └── TaskController.java    
├── service/
│   └── TaskService.java        
├── model/                         
│   ├── Task.java
│   ├── Staff.java
│   ├── TaskStatus.java
│   ├── Priority.java
│   ├── TaskComment.java
│   └── ActivityLog.java
└── dto/                         
    ├── CreateTaskRequest.java
    ├── AssignTaskRequest.java
    ├── UpdatePriorityRequest.java
    └── AddCommentRequest.java
```

## API Endpoints

### Task Management
- `POST /api/tasks` - Create new task
- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID (includes full history & comments)
- `GET /api/tasks/date-range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` - Smart daily view
- `GET /api/tasks/priority/{priority}` - Get tasks by priority (HIGH, MEDIUM, LOW)

### Task Operations
- `POST /api/tasks/assign-by-ref` - Assign task by customer reference
- `PUT /api/tasks/{id}/priority` - Update task priority
- `POST /api/tasks/{id}/comments` - Add comment to task

### Staff Management
- `GET /api/tasks/staff` - Get all staff members

## Video Demo

https://github.com/user-attachments/assets/972534e0-712e-4a4b-95a1-26229cbb65ff

## Quick Start

### Prerequisites
- Java 17 or higher
- Gradle (or use included wrapper)

### Running the Application

1. **Clone and Navigate**
   ```bash
   cd workforce-management-api
   ```

2. **Build the Application**
   ```bash
   ./gradlew build
   ```

3. **Run the Application**
   ```bash
   ./gradlew bootRun
   ```

4. **Access the API**
   - Base URL: `http://localhost:8080`
   - API endpoints: `http://localhost:8080/api/tasks/*`

### Sample Data
The application starts with pre-loaded sample staff:
- ID: 1, Name: "John Doe", Role: "Sales"
- ID: 2, Name: "Jane Smith", Role: "Operations"
- ID: 3, Name: "Bob Wilson", Role: "Sales"

## Testing the API

### Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Follow up with client",
    "description": "Contact client about proposal",
    "priority": "HIGH",
    "startDate": "2025-08-06T09:00:00",
    "dueDate": "2025-08-06T17:00:00",
    "assignedStaffId": 1,
    "customerReference": "CUST-001"
  }'
```

### Get Tasks by Date Range (Smart Daily View)
```bash
curl "http://localhost:8080/api/tasks/date-range?startDate=2025-08-06&endDate=2025-08-06"
```

### Assign Task by Customer Reference
```bash
curl -X POST http://localhost:8080/api/tasks/assign-by-ref \
  -H "Content-Type: application/json" \
  -d '{
    "customerReference": "CUST-001",
    "staffId": 2
  }'
```

### Add Comment to Task
```bash
curl -X POST http://localhost:8080/api/tasks/1/comments \
  -H "Content-Type: application/json" \
  -d '{
    "comment": "Client responded positively to proposal",
    "author": "John Doe"
  }'
```

## Architecture Decisions

### Data Storage
- **In-Memory Collections**: Using ConcurrentHashMap for thread-safe operations
- **Atomic Counters**: For generating unique IDs
- **No Database**: As per requirements, using Java collections instead

### Thread Safety
- **ConcurrentHashMap**: Ensures thread-safe access to task and staff data
- **AtomicLong**: Thread-safe ID generation

### Activity Logging
- **Automatic Logging**: All significant operations are automatically logged
- **Chronological Order**: Activities and comments are stored in order of occurrence
- **Detailed Context**: Each log entry includes timestamp, action, performer, and details

### Error Handling
- **Graceful Failures**: Proper error responses for invalid requests
- **Meaningful Messages**: Clear error messages for debugging
- **Exception Safety**: Proper handling of edge cases

## Key Implementation Details

### Bug Fixes
1. **Duplicate Task Prevention**: When reassigning by customer reference, the old task is marked as CANCELLED instead of remaining active
2. **Cancelled Task Filtering**: Date range queries automatically exclude CANCELLED tasks

### Smart Daily View
The date range endpoint implements intelligent filtering:
- Returns tasks that started within the specified date range
- PLUS active tasks that started before the range but are still open
- Excludes cancelled tasks for clean views

### Activity History
Every significant action is logged:
- Task creation, priority changes, reassignments
- Comment additions with truncated preview
- System-generated logs with detailed context

## Testing Scenarios

### Bug Fix Verification
1. **Test Duplicate Prevention**:
   - Create task with customer reference
   - Reassign to different staff
   - Verify old task is CANCELLED, new task is ACTIVE

2. **Test Cancelled Task Filtering**:
   - Create tasks and cancel some
   - Query by date range
   - Verify cancelled tasks are excluded

### Feature Testing
1. **Priority Management**:
   - Create tasks with different priorities
   - Update task priorities
   - Query tasks by priority level

2. **Comments and History**:
   - Add comments to tasks
   - View task details to see full history
   - Verify chronological ordering

## Contributing

This codebase follows Spring Boot best practices:
- Clean separation of concerns (Controller → Service → Model)
- RESTful API design
- Proper error handling and logging
- Thread-safe operations
- Comprehensive activity tracking

