package com.company;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class EmployeeAnalyzer {
Map<Integer, Employee> employees = new HashMap<>();
private Employee ceo;

public void loadEmployees(String fileName) throws IOException, URISyntaxException {
    //used to read the csv file from maven resource directory
    /*InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
    if (inputStream == null) {
        throw new FileNotFoundException("File not found: " + fileName);
    }*/
    String filePath = new File(Objects.requireNonNull(getClass().getClassLoader()
            .getResource(fileName)).toURI()).getAbsolutePath();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
   // try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String firstName = parts[1];
            String lastName = parts[2];
            double salary = Double.parseDouble(parts[3]);
            Integer managerId = parts.length > 4 && !parts[4].isEmpty() ? Integer.parseInt(parts[4]) : null;

            Employee emp = new Employee(id, firstName, lastName, salary, managerId);
            employees.put(id, emp);
            if (managerId == null) ceo = emp;
            System.out.println(emp);
        }
    }

    for (Employee emp : employees.values()) {
        if (emp.getManagerId() != null && employees.containsKey(emp.getManagerId())) {
            employees.get(emp.getManagerId()).getSubordinates().add(emp);
        }
    }
    // Debug print
    for (Employee emp : employees.values()) {
        System.out.println("Manager: " + emp.getFirstName() + " (ID: " + emp.getId() + ") has subordinates: " +
                emp.getSubordinates().stream().map(e -> e.getFirstName()).collect(Collectors.toList()));
    }
}

public void analyzeSalaries() {
    for (Employee manager : employees.values()) {
        if (!manager.getSubordinates().isEmpty()) {
            double avgSalary = manager.getSubordinates().stream().mapToDouble(e -> e.getSalary()).average().orElse(0);
            double minSalary = avgSalary * 1.2;
            double maxSalary = avgSalary * 1.5;

            if (manager.getSalary() < minSalary) {
                System.out.println("Underpaid manager: " + manager.getSalary() + " " + manager.getManagerId() + " (needs " + (minSalary - manager.getSalary()) + " more)");
            } else if (manager.getSalary() > maxSalary) {
                System.out.println("Overpaid manager: " + manager.getFirstName() + " " + manager.getLastName() + " (earns " + (manager.getSalary() - maxSalary) + " too much)");
            }
        }
    }
}

public void analyzeReportingDepth() {
    Map<Integer, Integer> depthMap = new HashMap<>();
    calculateDepth(ceo, 0, depthMap);
    for (Map.Entry<Integer, Integer> entry : depthMap.entrySet()) {
        if (entry.getValue() > 4) {
            Employee emp = employees.get(entry.getKey());
            System.out.println("Employee " + emp.getFirstName() + " " + emp.getLastName() + " has too many managers (" + entry.getValue() + " levels)");
        }
    }
}

private void calculateDepth(Employee emp, int depth, Map<Integer, Integer> depthMap) {
    depthMap.put(emp.getId(), depth);
    for (Employee sub : emp.getSubordinates()) {
        calculateDepth(sub, depth + 1, depthMap);
    }
}

public static void main(String[] args) throws IOException {
    EmployeeAnalyzer analyzer = new EmployeeAnalyzer();
    try {
        analyzer.loadEmployees("employees.csv");
    } catch (URISyntaxException e) {
        throw new RuntimeException(e);
    }
    analyzer.analyzeSalaries();
    analyzer.analyzeReportingDepth();
   }
}

