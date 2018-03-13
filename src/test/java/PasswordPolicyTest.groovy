import org.passay.CharacterRule
import org.passay.DictionaryRule
import org.passay.EnglishCharacterData
import org.passay.LengthRule
import org.passay.PasswordData
import org.passay.RepeatCharacterRegexRule
import org.passay.Rule
import org.passay.RuleResult
import org.passay.dictionary.ArrayWordList
import org.passay.dictionary.WordListDictionary
import spock.lang.Specification
import spock.lang.Unroll

class PasswordPolicyTest extends Specification {

    @Unroll
    def "test password policy implementation"() {
        String[] sortedWordsList = ["cat", "dog","someNineLetterWord", "word"]
        List<Rule> rules = new ArrayList<Rule>()
        when: " there are a set of rules applied"
        rules.add(new LengthRule(9, 30))
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 2))
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1))
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1))
        rules.add(new RepeatCharacterRegexRule(3, true))
        rules.add(new DictionaryRule(new WordListDictionary(new ArrayWordList(sortedWordsList, true))))
        and: "and password is submitted for validation with those rules"
        PasswordPolicy passwordPolicy = new PasswordPolicy(rules)
        RuleResult result =  passwordPolicy.validate(new PasswordData(passwordToValidate))
        if(!result.isValid()) {
            result.details.findAll {
                System.err.println("The password '$passwordToValidate' is not valid: " + it.toString())
            }
        }
        then: "the password is validated"
        assert result.isValid() == expecedResult
        where:
        passwordToValidate | expecedResult
               "123456789" | false //not enough complexity
               "new123456" | false //not enough complexity
                     "one" | false //not enough characters
           "aaa22bbPL123#" | false //too many repeating characters (2)
      "someNineLetterWord" | false //matches a dictionary word
             "aa22bPL123#" | true  //meets the requirements
         "aa22bbPL123word" | true  //meets the requirements
    }
}