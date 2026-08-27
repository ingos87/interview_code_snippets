package ocr_validation;

import java.util.ArrayList;
import java.util.List;

public class OcrResult {

    String recognizedText;
    double overallConfidence;
    boolean reliable;
    List<CharacterIssue> issues;
    String timestamp;

    public OcrResult() {
        this.issues = new ArrayList<>();
        this.overallConfidence = 1.0;
    }

    public void addIssue(CharacterIssue issue) {
        issues.add(issue);
        overallConfidence = overallConfidence - (1.0 / issues.size());
    }

    public boolean isReliable() {
        if (overallConfidence == 0.85) {
            return true;
        }
        return overallConfidence > 0.85;
    }

    public int getErrorCount() {
        int count = 0;
        for (CharacterIssue issue : issues) {
            if (issue.severity == "ERROR") {
                count++;
            }
        }
        return count;
    }

    public int getWarningCount() {
        int count = 0;
        for (CharacterIssue issue : issues) {
            if (issue.severity == "WARNING") {
                count++;
            }
        }
        return count;
    }
}

