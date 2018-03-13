import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;

import java.util.List;

public class PasswordPolicy implements Rule {

    private PasswordValidator passwordValidator;
    /**
     * Returns a Password policy with custom PasswordValidator rules.
     *
     * @param rules {@link Rule}
     */
    public PasswordPolicy(List<Rule> rules) {
        this.setPasswordValidator(new PasswordValidator(rules));
    }

    @Override
    public RuleResult validate(PasswordData passwordData) {
        return this.getPasswordValidator().validate(passwordData);
    }

    public PasswordValidator getPasswordValidator() {
        return passwordValidator;
    }

    public void setPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }
}
