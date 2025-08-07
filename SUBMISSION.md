# Workforce Management API - Submission

## Project Overview
This project implements a comprehensive Workforce Management API for a logistics super-app. The API provides functionality for creating, assigning, and tracking tasks for employees, with advanced features for priority management, task comments, and activity history.

### Key Features Implemented

#### Part 0: Project Setup ✅
- ✅ Created a fully functional Spring Boot project with Gradle
- ✅ Organized code into standard project structure:
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
      ├── TaskDto.java
      ├── CreateTaskRequest.java
      ├── AssignTaskRequest.java
      ├── UpdatePriorityRequest.java
      └── AddCommentRequest.java
  ```
- ✅ Configured build.gradle with all required dependencies (Spring Web, Lombok, MapStruct)

#### Part 1: Bug Fixes ✅

**Bug 1: Task Re-assignment Creates Duplicates**
- ✅ **Fixed**: Modified `assignTaskByReference()` method to mark the old task as CANCELLED when reassigning
- ✅ **Solution**: When a task is reassigned, the old task status is set to CANCELLED and a new task is created for the new staff member
- ✅ **Location**: `TaskService.java:89-115`

**Bug 2: Cancelled Tasks Clutter the View** 
- ✅ **Fixed**: Modified `getTasksByDateRange()` to filter out CANCELLED tasks
- ✅ **Solution**: Added filter to exclude tasks with status CANCELLED from date range queries
- ✅ **Location**: `TaskService.java:69-84`

#### Part 2: New Features ✅

**Feature 1: Smart Daily Task View**
- ✅ **Implemented**: Enhanced date-based task fetching logic
- ✅ **Functionality**: Returns all active tasks that started within date range PLUS active tasks that started before the range but are still open
- ✅ **Location**: `TaskService.java:69-84`

**Feature 2: Task Priority Management**
- ✅ **Added**: Priority field to Task model (HIGH, MEDIUM, LOW)
- ✅ **Endpoint**: `PUT /api/tasks/{id}/priority` - Change task priority
- ✅ **Endpoint**: `GET /api/tasks/priority/{priority}` - Fetch tasks by priority
- ✅ **Location**: `TaskController.java:68-76 & 78-84`

**Feature 3: Task Comments & Activity History**
- ✅ **Activity History**: Automatic logging of key events (task creation, priority changes, reassignments, comments)
- ✅ **User Comments**: Ability to add free-text comments to tasks
- ✅ **Endpoint**: `POST /api/tasks/{id}/comments` - Add comment to task
- ✅ **Viewing**: Complete activity history and comments included in task details, sorted chronologically
- ✅ **Location**: `TaskService.java:130-154 & addActivityLog method`

### Technical Implementation

#### Technology Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.0.4
- **Build Tool**: Gradle
- **Database**: In-memory Java collections (ConcurrentHashMap, List)
- **Dependencies**: Spring Web, Lombok

#### API Endpoints
- `POST /api/tasks` - Create new task
- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID (includes full history & comments)
- `GET /api/tasks/date-range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` - Smart daily view
- `GET /api/tasks/priority/{priority}` - Get tasks by priority
- `POST /api/tasks/assign-by-ref` - Assign task by customer reference
- `PUT /api/tasks/{id}/priority` - Update task priority
- `POST /api/tasks/{id}/comments` - Add comment to task
- `GET /api/tasks/staff` - Get all staff members

#### Data Models
- **Task**: Core entity with status, priority, dates, assignments, comments, and activity logs
- **Staff**: Employee information
- **TaskComment**: User comments with author and timestamp
- **ActivityLog**: System-generated activity history
- **Enums**: TaskStatus (ACTIVE, COMPLETED, CANCELLED), Priority (HIGH, MEDIUM, LOW)

#### Key Design Decisions
1. **Thread Safety**: Used ConcurrentHashMap for thread-safe operations
2. **Activity Logging**: Automatic system logging for all key operations
3. **Data Integrity**: Proper handling of task reassignments and status changes
4. **Smart Filtering**: Enhanced date range queries for better user experience

### Quality Assurance
- ✅ Clean, well-structured code with proper separation of concerns
- ✅ Comprehensive error handling with meaningful error messages
- ✅ Proper logging for debugging and monitoring
- ✅ RESTful API design following best practices
- ✅ Thread-safe implementation using concurrent collections

### 1. Link to your Public GitHub Repository
[GitHub Repository will be provided here]

### 2. Link to your Video Demonstration
[Video demonstration will be provided here]

## Running the Application
```bash
./gradlew bootRun
```
The application will start on `http://localhost:8080`

## Testing the API
Use any REST client (Postman, Insomnia, curl) to test the endpoints. Sample staff members are pre-loaded with IDs 1, 2, and 3.

