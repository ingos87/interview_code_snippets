package validation_report;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportGeneratorService {

    private static HashMap<String, ValidationReport> reportStorage = new HashMap<>();

    public ValidationReport generateReport(String addressId, String postalCode, String street, String city, String recipient, String country) {

        ValidationReport report = new ValidationReport(addressId);

        // Rule DP-001: Postal code format
        if (postalCode == null || postalCode.length() != 5) {
            Violation v = new Violation("postalCode", "Postal code must be exactly 5 characters", "DP-001", "ERROR");
            report.addViolation(v);
        } else {
            try {
                Integer.parseInt(postalCode);
            } catch (Exception e) {
                Violation v = new Violation("postalCode", "Postal code must contain only digits", "DP-001", "ERROR");
                report.addViolation(v);
            }
        }

        // Rule DP-002: Street name
        if (street == null || street.isEmpty()) {
            report.addViolation(new Violation("street", "Street must not be empty", "DP-002", "ERROR"));
        }
        if (street.length() > 40) {
            report.addViolation(new Violation("street", "Street name exceeds recommended length of 40 characters. Consider abbreviating.", "DP-002", "RECOMMENDATION"));
        }

        // Rule DP-003: City name
        if (city == null || city.trim().equals("")) {
            report.addViolation(new Violation("city", "City is required", "DP-003", "ERROR"));
        }
        if (city.length() > 30) {
            report.addViolation(new Violation("city", "City name is long, verify abbreviation options", "DP-003", "RECOMMENDATION"));
        }

        // Rule DP-004: Recipient
        if (recipient == null) {
            report.addViolation(new Violation("recipient", "Recipient must be specified", "DP-004", "ERROR"));
        }
        if (recipient.length() > 50) {
            report.addViolation(new Violation("recipient", "Recipient name exceeds 50 characters", "DP-004", "ERROR"));
        }
        if (recipient.contains("  ")) {
            report.addViolation(new Violation("recipient", "Recipient contains double spaces which may cause OCR issues", "DP-004", "RECOMMENDATION"));
        }

        // Rule DP-005: Country
        if (country == null || country.isEmpty()) {
            report.addViolation(new Violation("country", "Country is required for international mail", "DP-005", "ERROR"));
        } else if (!country.equals("DE") && !country.equals("AT") && !country.equals("CH")) {
            report.addViolation(new Violation("country", "Country not supported: " + country, "DP-005", "ERROR"));
        }

        if (report.status == null) {
            report.status = "VALID";
        }

        reportStorage.put(addressId, report);

        return report;
    }

    public ValidationReport getStoredReport(String addressId) {
        return reportStorage.get(addressId);
    }

    public List<ValidationReport> getAllReports() {
        return new ArrayList<>(reportStorage.values());
    }

    public void clearReports() {
        reportStorage.clear();
    }
}

