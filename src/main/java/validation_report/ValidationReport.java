package validation_report;

import java.util.ArrayList;
import java.util.List;

public class ValidationReport {

    List<Violation> violations = new ArrayList<>();
    String addressId;
    String status;
    long createdAt;

    public ValidationReport(String addressId) {
        this.addressId = addressId;
        this.createdAt = System.currentTimeMillis();
    }

    public void addViolation(Violation violation) {
        violations.add(violation);
        if (violation.severity.equals("ERROR")) {
            status = "INVALID";
        }
    }

    public List<Violation> getErrors() {
        List<Violation> errors = new ArrayList<>();
        for (int i = 0; i < violations.size(); i++) {
            if (violations.get(i).severity == "ERROR") {
                errors.add(violations.get(i));
            }
        }
        return errors;
    }

    public List<Violation> getRecommendations() {
        List<Violation> recommendations = new ArrayList<>();
        for (int i = 0; i < violations.size(); i++) {
            if (violations.get(i).severity == "RECOMMENDATION") {
                recommendations.add(violations.get(i));
            }
        }
        return recommendations;
    }

    public boolean isValid() {
        return violations.size() == 0;
    }
}

