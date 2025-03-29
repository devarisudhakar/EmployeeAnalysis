package com.company;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EmployeeAnalyzerTest {
    private EmployeeAnalyzer analyzer;
    private static final String TEST_FILE = "test_employees.csv";
    @Before
    public void setUp() throws IOException, URISyntaxException {
        createTestFile();
        analyzer = new EmployeeAnalyzer();

        analyzer.loadEmployees(TEST_FILE);
    }
    private void createTestFile() throws IOException {

        File file = new File(TEST_FILE);
        // Check if parent directory exists before calling mkdirs()
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
            // Delete old file if it exists and create new one
            Files.deleteIfExists(file.toPath());
        }

        List<String> lines = Arrays.asList(
                "Id,firstName,lastName,salary,managerId",
                "1,CEO,Chief,100000,",
                "2,Manager1,One,60000,1",
                "3,Manager2,Two,70000,1",
                "4,Employee1,Four,50000,2",
                "5,Employee2,Five,52000,2",
                "6,Employee3,Six,54000,2",
                "7,Employee4,Seven,56000,2",
                "8,DeepEmployee,Eight,40000,7",
                "9,TooDeep,Employee,30000,8",
                "10,VeryDeep,Person,35000,9"
        );

        Files.write(file.toPath(), lines);

    }
    @Test
    public void testLoadEmployees() {
        assertNotNull(analyzer);
        assertEquals(10, analyzer.employees.size());
    }
    @Test
    public void testSalaryAnalysis() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        analyzer.analyzeSalaries();
        String output = outContent.toString();
        assertTrue(output.contains("Underpaid manager"));
        assertTrue(output.contains("Overpaid manager"));
    }

    @Test
    public void testReportingDepthAnalysis() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        analyzer.analyzeReportingDepth();
        String output = outContent.toString();
        assertTrue(output.contains("has too many managers"));
    }
}


