package validation_report;

public class Violation {

    public String field;
    public String message;
    public String ruleReference;
    public String severity;
    public long timestamp;

    public Violation(String field, String message, String ruleReference, String severity) {
        this.field = field;
        this.message = message;
        this.ruleReference = ruleReference;
        this.severity = severity;
        this.timestamp = System.currentTimeMillis();
    }

    public String toString() {
        return severity + ": " + message + " (rule: " + ruleReference + ")";
    }
}

