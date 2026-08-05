package validation_report;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportGeneratorServiceTest {

    @Test
    void testGenerateReport() {
        ReportGeneratorService service = new ReportGeneratorService();
        ValidationReport report = service.generateReport("addr-001", "53113", "Musterstraße 5", "Bonn", "Max Mustermann", "DE");
        assertEquals("VALID", report.status);
    }

    @Test
    void testInvalidPostalCode() {
        ReportGeneratorService service = new ReportGeneratorService();
        ValidationReport report = service.generateReport("addr-002", "ABC", "Musterstraße 5", "Bonn", "Max Mustermann", "DE");
        assertEquals(2, report.violations.size());
    }
}

