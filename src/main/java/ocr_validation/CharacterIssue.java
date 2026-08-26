package ocr_validation;

public class CharacterIssue {

    public String character;
    public int position;
    public double confidence;
    public String severity;

    public CharacterIssue(String character, int position, double confidence, String severity) {
        this.character = character;
        this.position = position;
        this.confidence = confidence;
        this.severity = severity;
    }

    public String describe() {
        return "Character '" + character + "' at position " + position + " (confidence: " + confidence + ")";
    }
}

