# Workforce Management API - Testing Guide

This guide provides comprehensive API testing examples to demonstrate all features and bug fixes.

## Prerequisites
- Application running on `http://localhost:8080`
- curl command-line tool (or any REST client like Postman)

## Quick Test Script

### 1. Test Application Status
```bash
# Check if application is running
curl -s http://localhost:8080/api/tasks/staff
# Expected: JSON array with 3 staff members
```

## Complete Feature Testing

### Step 1: Get Initial Staff Data
```bash
curl -X GET http://localhost:8080/api/tasks/staff
```
**Expected Response:**
```json
[
  {"id":1,"name":"John Doe","email":"john@company.com","role":"Sales"},
  {"id":2,"name":"Jane Smith","email":"jane@company.com","role":"Operations"},
  {"id":3,"name":"Bob Wilson","email":"bob@company.com","role":"Sales"}
]
```

### Step 2: Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Follow up with client ABC",
    "description": "Contact client about new proposal and pricing",
    "priority": "HIGH",
    "startDate": "2025-08-07T09:00:00",
    "dueDate": "2025-08-07T17:00:00",
    "assignedStaffId": 1,
    "customerReference": "CUST-ABC-001"
  }'
```
**Expected:** Task created with ID 1, status ACTIVE, assigned to staff ID 1

### Step 3: Create Another Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Process order XYZ",
    "description": "Handle order processing and shipping",
    "priority": "MEDIUM",
    "startDate": "2025-08-06T10:00:00",
    "dueDate": "2025-08-08T16:00:00",
    "assignedStaffId": 2,
    "customerReference": "CUST-XYZ-002"
  }'
```

## Bug Fix Testing

### Bug Fix 1: Task Re-assignment (No More Duplicates)

#### Step 1: View All Tasks Before Reassignment
```bash
curl -X GET http://localhost:8080/api/tasks
```

#### Step 2: Reassign Task by Customer Reference
```bash
curl -X POST http://localhost:8080/api/tasks/assign-by-ref \
  -H "Content-Type: application/json" \
  -d '{
    "customerReference": "CUST-ABC-001",
    "staffId": 3
  }'
```
**Expected:** 
- New task created with same details but assigned to staff ID 3
- Activity log shows reassignment details

#### Step 3: Verify Old Task is Cancelled
```bash
curl -X GET http://localhost:8080/api/tasks
```
**Expected:** 
- Original task (ID 1) should have status "CANCELLED"
- New task should have status "ACTIVE" 
- No duplicate active tasks for same customer reference

### Bug Fix 2: Date Range Filtering (No Cancelled Tasks)

#### Step 1: Create Task and Then Cancel It
```bash
# Create task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test task for cancellation",
    "description": "This task will be cancelled to test filtering",
    "priority": "LOW",
    "startDate": "2025-08-07T11:00:00",
    "dueDate": "2025-08-07T18:00:00",
    "assignedStaffId": 2,
    "customerReference": "CUST-TEST-003"
  }'

# Reassign to cancel the original
curl -X POST http://localhost:8080/api/tasks/assign-by-ref \
  -H "Content-Type: application/json" \
  -d '{
    "customerReference": "CUST-TEST-003",
    "staffId": 1
  }'
```

#### Step 2: Test Date Range Filtering
```bash
curl "http://localhost:8080/api/tasks/date-range?startDate=2025-08-07&endDate=2025-08-07"
```
**Expected:** Only ACTIVE tasks should be returned, CANCELLED tasks should be excluded

## Feature Testing

### Feature 1: Smart Daily Task View

#### Test 1: Tasks in Date Range
```bash
curl "http://localhost:8080/api/tasks/date-range?startDate=2025-08-07&endDate=2025-08-07"
```

#### Test 2: Tasks Started Before Range But Still Active
```bash
# This should include the task that started on 2025-08-06 but is still ACTIVE
curl "http://localhost:8080/api/tasks/date-range?startDate=2025-08-07&endDate=2025-08-07"
```
**Expected:** Includes tasks from Aug 7 AND active tasks that started earlier (like Aug 6 task)

### Feature 2: Priority Management

#### Step 1: Update Task Priority
```bash
# Get active task ID first, then update its priority
curl -X PUT http://localhost:8080/api/tasks/2/priority \
  -H "Content-Type: application/json" \
  -d '{"priority": "HIGH"}'
```
**Expected:** 
- Priority updated to HIGH
- Activity log shows "Priority changed from MEDIUM to HIGH"

#### Step 2: Get Tasks by Priority
```bash
curl -X GET http://localhost:8080/api/tasks/priority/HIGH
```
**Expected:** Returns all tasks with HIGH priority

#### Step 3: Test All Priority Levels
```bash
curl -X GET http://localhost:8080/api/tasks/priority/HIGH
curl -X GET http://localhost:8080/api/tasks/priority/MEDIUM  
curl -X GET http://localhost:8080/api/tasks/priority/LOW
```

### Feature 3: Comments & Activity History

#### Step 1: Add Comment to Task
```bash
curl -X POST http://localhost:8080/api/tasks/2/comments \
  -H "Content-Type: application/json" \
  -d '{
    "comment": "Client confirmed the order details. Proceeding with processing.",
    "author": "Jane Smith"
  }'
```

#### Step 2: Add Another Comment
```bash
curl -X POST http://localhost:8080/api/tasks/2/comments \
  -H "Content-Type: application/json" \
  -d '{
    "comment": "Order shipped via FedEx. Tracking number: 1234567890",
    "author": "Bob Wilson"
  }'
```

#### Step 3: View Complete Task History
```bash
curl -X GET http://localhost:8080/api/tasks/2
```
**Expected Response Should Include:**
- Complete task details
- All comments with timestamps and authors
- Complete activity history in chronological order:
  - Task creation
  - Priority changes
  - Comment additions
  - Any reassignments

## Advanced Testing Scenarios

### Scenario 1: Complete Task Lifecycle
```bash
# 1. Create task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project review",
    "description": "Review all project deliverables",
    "priority": "MEDIUM",
    "startDate": "2025-08-07T09:00:00",
    "dueDate": "2025-08-10T17:00:00",
    "assignedStaffId": 1,
    "customerReference": "PROJ-REVIEW-001"
  }'

# 2. Change priority
curl -X PUT http://localhost:8080/api/tasks/[TASK_ID]/priority \
  -H "Content-Type: application/json" \
  -d '{"priority": "HIGH"}'

# 3. Add comment
curl -X POST http://localhost:8080/api/tasks/[TASK_ID]/comments \
  -H "Content-Type: application/json" \
  -d '{
    "comment": "Started initial review phase",
    "author": "John Doe"
  }'

# 4. Reassign task
curl -X POST http://localhost:8080/api/tasks/assign-by-ref \
  -H "Content-Type: application/json" \
  -d '{
    "customerReference": "PROJ-REVIEW-001",
    "staffId": 2
  }'

# 5. View final task with complete history
curl -X GET http://localhost:8080/api/tasks
```

### Scenario 2: Date Range Intelligence Test
```bash
# Create tasks with different start dates
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Task started yesterday",
    "startDate": "2025-08-06T09:00:00",
    "dueDate": "2025-08-08T17:00:00",
    "assignedStaffId": 1,
    "customerReference": "YESTERDAY-001"
  }'

curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Task starting today",
    "startDate": "2025-08-07T09:00:00", 
    "dueDate": "2025-08-07T17:00:00",
    "assignedStaffId": 2,
    "customerReference": "TODAY-001"
  }'

# Query for today's tasks - should include both
curl "http://localhost:8080/api/tasks/date-range?startDate=2025-08-07&endDate=2025-08-07"
```

## Validation Points

✅ **Bug Fix 1 Verification:**
- Old tasks are marked CANCELLED when reassigned
- New tasks are created for reassignment
- No duplicate active tasks for same customer reference

✅ **Bug Fix 2 Verification:** 
- Date range queries exclude CANCELLED tasks
- Only ACTIVE tasks are returned in date range results

✅ **Feature 1 Verification:**
- Date range includes tasks starting in range
- Date range includes active tasks that started before range
- Smart filtering provides comprehensive daily view

✅ **Feature 2 Verification:**
- Task priorities can be updated
- Tasks can be queried by priority level
- Priority changes are logged in activity history

✅ **Feature 3 Verification:**
- Comments can be added with author and timestamp
- Activity history automatically tracks all operations
- Task details include complete chronological history

## Notes
- Replace `[TASK_ID]` with actual task ID from previous responses
- All timestamps are in ISO format
- Activity logs are automatically generated for all operations
- Comments and activity logs are sorted chronologically