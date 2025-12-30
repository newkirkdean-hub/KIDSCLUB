# Tip Distribution Calculator

A React-based web application for calculating tip distribution among different employee roles based on predefined percentages.

## Features

1. **Manual Inputs:**
   - Users can update the `# of Employees` for each role
   - Users can input a grand total for `Total Pooled Tips`

2. **Dynamic Calculations:**
   - The `Total Tips for Role` dynamically updates as `Tip % * Total Pooled Tips`
   - The `Tip per Employee` dynamically updates as `Total Tips for Role / # of Employees`

3. **Table Display:**
   - Clean, intuitive interface for viewing and editing tip distribution
   - Responsive design for different screen sizes

## File Structure

- `src/App.js`: Contains the main logic for rendering the table and updating the calculations
- `src/data.js`: Contains the static roles and percentages to maintain a clean structure
- `src/styles.css`: Adds simple styling for an intuitive interface
- `src/index.js`: Entry point that renders the React app
- `public/index.html`: HTML template

## Installation

1. Navigate to the tip-calculator directory:
   ```bash
   cd tip-calculator
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

## Running the Application

To start the development server:

```bash
npm start
```

The application will open in your browser at `http://localhost:3000`.

## Building for Production

To create a production build:

```bash
npm run build
```

The optimized files will be in the `build` directory.

## Usage

1. Enter the total pooled tips amount in the "Total Pooled Tips" field
2. Adjust the number of employees for each role using the input fields in the table
3. The calculations will update automatically:
   - Total Tips for Role = (Tip % / 100) × Total Pooled Tips
   - Tip per Employee = Total Tips for Role / # of Employees

## Default Role Distribution

- Manager: 15%
- Server: 40%
- Bartender: 25%
- Busser: 10%
- Host: 10%

Total: 100%

## Customization

To modify the roles and percentages, edit the `src/data.js` file.
