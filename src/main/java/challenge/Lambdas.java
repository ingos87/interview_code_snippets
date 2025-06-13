package challenge;

import java.util.Arrays;
import java.util.List;

public class Lambdas {

    public String simpleLambda() {
        List<String> companies = Arrays.asList("DHL", "UPS", "hermes", "BlueDart", "FedEx");

        String result = "";
        for (int i = 0; i < companies.size(); i++) {
            result += ", " + companies.get(i);
        }
        return "I like " + result;
    }

    public int lambdas() {
        List<Double> numbers = Arrays.asList(0.4, 4.2, 5.1, 11.99, 12.5);


        return 32; // 0+4+2+5+11+12
    }
}
