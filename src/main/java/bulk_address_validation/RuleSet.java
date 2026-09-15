package bulk_address_validation;

public class RuleSet {

    public String countryCode;
    public String countryName;
    public int minPostalCodeLength;
    public int maxPostalCodeLength;
    public String postalCodePattern;
    public String[] requiredFields;

    public RuleSet(String countryCode, String countryName, int minPostalCodeLength,
                   int maxPostalCodeLength, String postalCodePattern, String[] requiredFields) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.minPostalCodeLength = minPostalCodeLength;
        this.maxPostalCodeLength = maxPostalCodeLength;
        this.postalCodePattern = postalCodePattern;
        this.requiredFields = requiredFields;
    }
}


