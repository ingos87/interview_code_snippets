package configurable_rules;

public class PostalRule {

    private String name;
    private String pattern;
    private int maxLength;
    private String version;
    private boolean active;

    public PostalRule(String name, String pattern, int maxLength, String version, boolean active) {
        this.name = name;
        this.pattern = pattern;
        this.maxLength = maxLength;
        this.version = version;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getVersion() {
        return version;
    }

    public boolean isActive() {
        return active;
    }
}

