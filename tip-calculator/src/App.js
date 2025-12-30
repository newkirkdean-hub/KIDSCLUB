import React, { useState } from 'react';
import { rolesData } from './data';
import './styles.css';

function App() {
  // State for total pooled tips
  const [totalPooledTips, setTotalPooledTips] = useState(0);
  
  // State for number of employees per role
  const [employeeCounts, setEmployeeCounts] = useState(
    rolesData.reduce((acc, role) => {
      acc[role.role] = 1; // Default to 1 employee per role
      return acc;
    }, {})
  );

  // Handle change in total pooled tips
  const handleTotalTipsChange = (e) => {
    const value = e.target.value === '' ? 0 : parseFloat(e.target.value) || 0;
    setTotalPooledTips(value);
  };

  // Handle change in employee count for a specific role
  const handleEmployeeCountChange = (role, e) => {
    const value = e.target.value === '' ? 0 : parseInt(e.target.value) || 0;
    setEmployeeCounts({
      ...employeeCounts,
      [role]: value
    });
  };

  // Calculate total tips for a role
  const calculateTotalTipsForRole = (percentage) => {
    return (percentage / 100) * totalPooledTips;
  };

  // Calculate tip per employee for a role
  const calculateTipPerEmployee = (percentage, employeeCount) => {
    if (employeeCount === 0) return 0;
    const totalForRole = calculateTotalTipsForRole(percentage);
    return totalForRole / employeeCount;
  };

  return (
    <div className="app-container">
      <h1>Tip Distribution Calculator</h1>
      
      <div className="total-tips-section">
        <label htmlFor="totalTips">
          Total Pooled Tips ($):
        </label>
        <input
          id="totalTips"
          type="number"
          min="0"
          step="0.01"
          value={totalPooledTips}
          onChange={handleTotalTipsChange}
          placeholder="Enter total tips"
        />
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Role</th>
              <th>Tip %</th>
              <th># of Employees</th>
              <th>Total Tips for Role</th>
              <th>Tip per Employee</th>
            </tr>
          </thead>
          <tbody>
            {rolesData.map((roleData) => {
              const employeeCount = employeeCounts[roleData.role];
              const totalForRole = calculateTotalTipsForRole(roleData.percentage);
              const tipPerEmployee = calculateTipPerEmployee(roleData.percentage, employeeCount);
              
              return (
                <tr key={roleData.role}>
                  <td>{roleData.role}</td>
                  <td>{roleData.percentage}%</td>
                  <td>
                    <input
                      type="number"
                      min="0"
                      value={employeeCount}
                      onChange={(e) => handleEmployeeCountChange(roleData.role, e)}
                      className="employee-input"
                    />
                  </td>
                  <td>${totalForRole.toFixed(2)}</td>
                  <td>${tipPerEmployee.toFixed(2)}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <div className="summary">
        <p>Total percentage allocated: {rolesData.reduce((sum, role) => sum + role.percentage, 0)}%</p>
      </div>
    </div>
  );
}

export default App;
