# Employee Password Security Improvements

The following document provides suggestions for improving the employee security system in the KIDSCLUB program. These ideas will help guide future development and ensure a sustainable and secure approach for managing access control:

## Current System Overview
- Employees are assigned a numerical level of access (e.g., 1 for beginner, 3 for master) stored in a simple employee table.
- Users enter an employee number to access the program, and the database checks their validity and whether they are active.
- The program runs on a local network and is hidden on the file system to limit user access.

---

## Suggested Improvements

### 1. **Employee Table and Access Levels**
**Current Approach:**
- Numerical access levels (e.g., 1, 2, 3) that represent the employee's roles.

**Improvement:**
- Add more granular roles (e.g., `viewer`, `editor`, `administrator`) to allow specific permissions.
- Document and clarify the meaning of these levels for future developers.

### 2. **Employee Authentication**
**Current Approach:**
- Authentication via an employee number.

**Improvement:**
- Add a PIN or password associated with each employee number.
- Flag and log failed authentication attempts to improve security monitoring.

### 3. **Database Integrity**
**Current Approach:**
- The database validates the employee's active/inactive status and access level.

**Improvement:**
- Store an explicit "active/inactive" flag for more manageable access control.
- Monitor and log any updates to database records.

### 4. **Pop-Up Access**
**Current Approach:**
- Input window pops up for users to enter their credentials.

**Improvement:**
- Enforce a timeout mechanism to automatically close inactive pop-ups.
- Validate all input fields to prevent invalid or malicious data.

### 5. **Security and Program Accessibility**
**Current Approach:**
- Program files are hidden from the file system to limit access.

**Improvement:**
- Restrict file permissions so only authorized accounts can run or edit the program.
- Avoid reliance solely on "hiding files," as it provides limited security.

### 6. **Documentation for Future Programmers**
- Maintain clear documentation on:
  - Structure of the employee table.
  - Logic for access level validation.
  - Steps for adding/removing employees.
  - Database schema or external dependencies.

### 7. **Scalability**
**Current Approach:**
- Simple file-based database for a small number of employees.

**Improvement:**
- Explore transitioning to a larger database solution, such as SQLite or PostgreSQL, for future scalability.

### 8. **Security for the Local Environment**
- Keep program files non-writable for unauthorized users.
- Use encryption for employee data at rest.
- Plan for disaster recovery by implementing server backups.

---

These recommendations focus on improving the security, scalability, and maintainability of the current implementation while remaining accessible to new developers.