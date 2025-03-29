1.My Assumptions:
  The first row of the CSV file contains headers (i.e., column names: Id,firstName,lastName,salary,managerId).
  If an employee does not have a manager, the managerId field is empty (not null, but an empty string).
  Id and managerId are integers, while salary is a numeric value (assumed to be in whole numbers, no decimals).

2.Salary Analysis Assumptions:

  A manager is defined as any employee who has at least one direct subordinate.
  A manager's salary should be at least 20% more than the average salary of their direct subordinates but not more than 50% more than the average.
  If a manager is underpaid, the amount they need to increase is calculated as: 
    requiredSalary = avgSubordinateSalary * 1.2
    underpaidAmount = requiredSalary - managerSalary
 If a manager is overpaid, the excess amount is calculated as:
   maxAllowedSalary = avgSubordinateSalary * 1.5
   overpaidAmount = managerSalary - maxAllowedSalary
 
3. Organizational Structure Assumptions

  The CEO is the only employee without a manager (managerId is empty).
  All other employees have a valid managerId that corresponds to an existing employee.
  An employee's "reporting depth" is determined by the number of managers in the hierarchy between them and the CEO.
  If an employee has more than 4 managers above them, their reporting line is too long, and the excess depth is calculated as:
  excessDepth = depth - 4
