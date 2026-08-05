package configurable_rules;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleConfigurationService {

    private static List<PostalRule> rules = new ArrayList<>();
    private static List<PostalRule> previousRules = new ArrayList<>();

    public List<PostalRule> loadRules() {
        previousRules = rules;
        rules = new ArrayList<>();

        try {
            File file = new File("/etc/postal-rules/rules.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                PostalRule rule = new PostalRule(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4].equals("true"));
                rules.add(rule);
            }
        } catch (Exception e) {
            System.out.println("Could not load rules: " + e.getMessage());
        }

        return rules;
    }

    public void reloadRules() {
        loadRules();
    }

    public List<PostalRule> getRules() {
        if (rules.size() == 0) {
            loadRules();
        }
        return rules;
    }

    public List<PostalRule> getPreviousRules() {
        return previousRules;
    }

    public PostalRule getRuleByName(String name) {
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getName().equals(name)) {
                return rules.get(i);
            }
        }
        return null;
    }
}

