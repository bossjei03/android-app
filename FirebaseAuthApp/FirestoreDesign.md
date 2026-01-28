# Firebase Firestore Database Structure for School App

This document outlines the proposed Firestore database structure for a school app with admin, teacher, and student roles. The structure is designed to be scalable, secure, and efficient for querying.

## Overview
- **Collections**: Top-level containers for different types of data.
- **Documents**: Individual records within collections.
- **Subcollections**: Nested collections under documents for related data.
- **Security Rules**: Firestore rules should be implemented to restrict access based on user roles.

## Collections and Structure

### 1. `users` Collection
Stores user information and roles.

**Document ID**: User UID from Firebase Auth.

**Fields**:
- `email` (string): User's email address.
- `role` (string): "admin", "teacher", or "student".
- `firstName` (string): User's first name.
- `lastName` (string): User's last name.
- `createdAt` (timestamp): Account creation date.
- `updatedAt` (timestamp): Last update date.

**Subcollections**:
- None directly, but referenced in other collections.

**Security**:
- Users can read/write their own document.
- Admins can read/write all user documents.
- Teachers can read student documents in their classes.

### 2. `classes` Collection
Stores class information.

**Document ID**: Auto-generated or custom class code.

**Fields**:
- `name` (string): Class name (e.g., "Mathematics 101").
- `subject` (string): Subject name.
- `teacherId` (string): UID of the teacher.
- `description` (string): Class description.
- `schedule` (map): Schedule information (days, times).
- `createdAt` (timestamp): Class creation date.
- `updatedAt` (timestamp): Last update date.

**Subcollections**:
- `students`: References to student UIDs enrolled in the class.
- `announcements`: Class-specific announcements.

**Security**:
- Teachers can read/write their own classes.
- Students can read classes they're enrolled in.
- Admins have full access.

### 3. `subjects` Collection
Stores subject information.

**Document ID**: Auto-generated.

**Fields**:
- `name` (string): Subject name (e.g., "Mathematics").
- `description` (string): Subject description.
- `gradeLevel` (string): Applicable grade level.
- `createdAt` (timestamp): Subject creation date.

**Subcollections**:
- None.

**Security**:
- Read access for all authenticated users.
- Write access for admins and teachers.

### 4. `assignments` Collection
Stores assignment information.

**Document ID**: Auto-generated.

**Fields**:
- `title` (string): Assignment title.
- `description` (string): Assignment description.
- `classId` (string): Reference to class document.
- `teacherId` (string): UID of the teacher who created it.
- `dueDate` (timestamp): Assignment due date.
- `createdAt` (timestamp): Assignment creation date.
- `updatedAt` (timestamp): Last update date.

**Subcollections**:
- `submissions`: Student submissions for this assignment.

**Security**:
- Teachers can read/write assignments for their classes.
- Students can read assignments for their classes.
- Students can submit to assignments they're enrolled in.

### 5. `grades` Collection
Stores grade information.

**Document ID**: Auto-generated.

**Fields**:
- `studentId` (string): UID of the student.
- `assignmentId` (string): Reference to assignment document.
- `grade` (number): Numerical grade.
- `feedback` (string): Teacher feedback.
- `gradedBy` (string): UID of the teacher who graded.
- `gradedAt` (timestamp): Grading timestamp.

**Subcollections**:
- None.

**Security**:
- Students can read their own grades.
- Teachers can read/write grades for students in their classes.
- Admins have full access.

### 6. `announcements` Collection
Stores school-wide announcements.

**Document ID**: Auto-generated.

**Fields**:
- `title` (string): Announcement title.
- `content` (string): Announcement content.
- `authorId` (string): UID of the author (admin or teacher).
- `targetAudience` (string): "all", "teachers", "students", or specific class IDs.
- `createdAt` (timestamp): Announcement creation date.
- `updatedAt` (timestamp): Last update date.

**Subcollections**:
- None.

**Security**:
- Admins and teachers can create announcements.
- All users can read announcements targeted to them.

### 7. `attendance` Collection
Stores attendance records.

**Document ID**: Auto-generated.

**Fields**:
- `studentId` (string): UID of the student.
- `classId` (string): Reference to class document.
- `date` (timestamp): Attendance date.
- `status` (string): "present", "absent", "late".
- `markedBy` (string): UID of the person marking attendance.
- `markedAt` (timestamp): Timestamp when marked.

**Subcollections**:
- None.

**Security**:
- Teachers can read/write attendance for their classes.
- Students can read their own attendance.
- Admins have full access.

## Relationships and References
- Users are referenced by UID in various collections.
- Classes reference teachers and subjects.
- Assignments reference classes and teachers.
- Grades reference students and assignments.
- Attendance references students and classes.

## Indexing Considerations
- Create composite indexes for frequently queried combinations:
  - `classes.teacherId` + `classes.subject`
  - `assignments.classId` + `assignments.dueDate`
  - `grades.studentId` + `grades.assignmentId`
  - `attendance.studentId` + `attendance.classId` + `attendance.date`

## Security Rules Example
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can read/write their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }

    // Admins have full access
    match /{document=**} {
      allow read, write: if get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }

    // Teachers can access their classes and students
    match /classes/{classId} {
      allow read, write: if get(/databases/$(database)/documents/classes/$(classId)).data.teacherId == request.auth.uid;
    }

    // Students can read their enrolled classes
    match /classes/{classId} {
      allow read: if exists(/databases/$(database)/documents/classes/$(classId)/students/$(request.auth.uid));
    }

    // Similar rules for other collections...
  }
}
```

## Scalability Notes
- Use pagination for large lists (e.g., all students in a class).
- Implement caching in the app for frequently accessed data.
- Consider using Firebase Cloud Functions for complex operations or aggregations.

## Future Extensions
- Add `schools` collection for multi-school support.
- Include `parents` collection for parent-teacher communication.
- Add `resources` collection for shared learning materials.
- Implement `notifications` for push notifications.

This structure provides a solid foundation for a school app with proper role-based access control.
