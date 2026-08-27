package international_validation;

public class CountryRule {

    public String countryCode;
    public String countryName;
    public int postalCodeLength;
    public String postalCodePattern;
    public boolean requiresState;
    public String[] requiredFields;

    public CountryRule(String countryCode, String countryName, int postalCodeLength,
                       String postalCodePattern, boolean requiresState, String[] requiredFields) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.postalCodeLength = postalCodeLength;
        this.postalCodePattern = postalCodePattern;
        this.requiresState = requiresState;
        this.requiredFields = requiredFields;
    }

    public String describe() {
        return "Country: " + countryName + " (" + countryCode + ")";
    }
}

