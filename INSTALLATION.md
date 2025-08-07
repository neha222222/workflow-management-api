


# Workforce Management API - Installation & Setup Guide

This guide will help you install and run the Workforce Management API from scratch, even if you don't have Java installed on your system.

## Prerequisites

This application requires:
- Java 17 or higher
- Internet connection (for downloading dependencies)

## Step-by-Step Installation

### 1. Install Java (if not already installed)

#### For macOS:
```bash
# Install using Homebrew (recommended)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
brew install openjdk@17

# Or download from Oracle: https://www.oracle.com/java/technologies/downloads/#java17
```

#### For Windows:
1. Download Java 17 from: https://www.oracle.com/java/technologies/downloads/#java17
2. Run the installer and follow the setup wizard
3. Add Java to your PATH environment variable

#### For Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

#### For Linux (CentOS/RHEL):
```bash
sudo yum install java-17-openjdk-devel
```

### 2. Verify Java Installation
```bash
java --version
```
You should see output similar to:
```
openjdk 17.0.x or higher
```

### 3. Extract and Navigate to Project
```bash
# Extract the zip file
unzip workforce-management-api.zip
cd workforce-management-api
```

### 4. Make Gradle Wrapper Executable (Unix/Linux/macOS)
```bash
chmod +x gradlew
```

### 5. Build the Application
```bash
# On Unix/Linux/macOS:
./gradlew build

# On Windows:
gradlew.bat build
```

This will:
- Download all required dependencies
- Compile the application
- Run tests
- Create the executable JAR file

### 6. Run the Application
```bash
# On Unix/Linux/macOS:
./gradlew bootRun

# On Windows:
gradlew.bat bootRun
```

The application will start and you'll see output like:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

Tomcat started on port(s): 8080 (http)
Started WorkforcemgmtApplication in X.XXX seconds
```

### 7. Test the Installation
Open a new terminal and test the API:
```bash
# Test the staff endpoint
curl http://localhost:8080/api/tasks/staff

# Expected output: List of 3 staff members
```

## Running the Application

### Start the Application
```bash
./gradlew bootRun  # Unix/Linux/macOS
gradlew.bat bootRun  # Windows
```

### Stop the Application
Press `Ctrl+C` in the terminal where the application is running.

### Run in Background (Unix/Linux/macOS)
```bash
nohup ./gradlew bootRun > app.log 2>&1 &
```

## API Endpoints

Once running, the API is available at `http://localhost:8080`

### Core Endpoints:
- `GET /api/tasks/staff` - Get all staff members
- `POST /api/tasks` - Create new task
- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID (with full history)
- `GET /api/tasks/date-range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` - Smart daily view
- `GET /api/tasks/priority/{HIGH|MEDIUM|LOW}` - Get tasks by priority
- `POST /api/tasks/assign-by-ref` - Reassign task by customer reference
- `PUT /api/tasks/{id}/priority` - Update task priority
- `POST /api/tasks/{id}/comments` - Add comment to task

## Sample API Usage

### 1. Get All Staff
```bash
curl http://localhost:8080/api/tasks/staff
```

### 2. Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Follow up with client",
    "description": "Contact client about proposal",
    "priority": "HIGH",
    "startDate": "2025-08-07T09:00:00",
    "dueDate": "2025-08-07T17:00:00",
    "assignedStaffId": 1,
    "customerReference": "CUST-001"
  }'
```

### 3. Get Tasks by Date Range
```bash
curl "http://localhost:8080/api/tasks/date-range?startDate=2025-08-07&endDate=2025-08-07"
```

### 4. Reassign Task
```bash
curl -X POST http://localhost:8080/api/tasks/assign-by-ref \
  -H "Content-Type: application/json" \
  -d '{
    "customerReference": "CUST-001",
    "staffId": 2
  }'
```

### 5. Add Comment
```bash
curl -X POST http://localhost:8080/api/tasks/1/comments \
  -H "Content-Type: application/json" \
  -d '{
    "comment": "Client responded positively",
    "author": "Jane Smith"
  }'
```

## Troubleshooting

### Common Issues:

#### 1. "java: command not found"
- Java is not installed or not in PATH
- Follow Step 1 to install Java properly

#### 2. "Permission denied: ./gradlew"
- Run: `chmod +x gradlew` (Unix/Linux/macOS)

#### 3. "Port 8080 already in use"
- Another application is using port 8080
- Kill the other process or change the port in `src/main/resources/application.properties`

#### 4. "Failed to connect to localhost:8080"
- Application might still be starting (wait 10-30 seconds)
- Check if the application is running: `ps aux | grep java`

#### 5. Build fails with Java version errors
- Make sure you have Java 17 or higher installed
- Check with: `java --version`

## Application Features

✅ **Complete Task Management System**
- Create, assign, and track tasks
- Priority management (HIGH, MEDIUM, LOW)
- Task status tracking (ACTIVE, COMPLETED, CANCELLED)

✅ **Bug Fixes Implemented**
- Task reassignment prevents duplicates (old tasks marked as CANCELLED)
- Date range queries exclude cancelled tasks

✅ **Advanced Features**
- Smart daily view (includes active tasks from range + earlier active tasks)
- Comprehensive activity logging
- User comments with timestamps
- Full task history tracking

## Data Storage

The application uses in-memory storage, so data will be lost when the application stops. This is by design for the demo. Sample staff data is automatically loaded on startup.

## Support

For technical issues or questions about the API functionality, refer to:
- Application logs in the terminal
- README.md for detailed API documentation
- SUBMISSION.md for implementation details

---

**Workforce Management API v1.0**  
Built with Spring Boot 3.0.4 and Java 17+