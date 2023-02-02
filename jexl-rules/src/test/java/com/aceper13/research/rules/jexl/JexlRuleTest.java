package com.aceper13.research.rules.jexl;

import com.acepero13.research.ruleengine.api.Facts;
import com.acepero13.research.ruleengine.api.Rule;
import com.acepero13.research.ruleengine.api.RuleEngine;
import com.acepero13.research.ruleengine.core.engines.DefaultRuleEngine;
import com.acepero13.research.ruleengine.model.InMemoryFacts;
import com.acepero13.research.ruleengine.model.Rules;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class JexlRuleTest {

    private Facts facts = new InMemoryFacts();
    ;


    @Test
    void personRule() {

        Person tom = new Person("Tom", 14);
        facts.put("person", tom);
        Rule ageRule = new JexlRuleBuilder.Builder()
                .name("age rule")
                .description("Check if person's age is > 18 and marks the person as adult")
                .priority(1)
                .when("person.age > 18")
                .then("person.setAdult(true);")
                .build();

        Rule alcoholRule = new JexlRuleBuilder.Builder()
                .name("alcohol rule")
                .description("children are not allowed to buy alcohol")
                .priority(2)
                .when("person.isAdult() == false")
                .then("facts.put('forbidden', true)")
                .build();

        RuleEngine engine = new DefaultRuleEngine(new Rules(ageRule, alcoholRule));
        engine.fire(facts);

        assertTrue(facts.exists("forbidden"));

    }

    @Test
    void readRulesFromFile() throws IOException {
        Rule ageRule = JexlRuleBuilder.of(load("age_rule.yaml"));
        Rule alcoholRule = JexlRuleBuilder.of(load("alcohol_rule.yaml"));
        Person tom = new Person("Tom", 14);
        facts.put("person", tom);

        RuleEngine engine = new DefaultRuleEngine(new Rules(ageRule, alcoholRule));
        engine.fire(facts);

        assertTrue(facts.exists("forbidden"));
    }

    private InputStream load(String filename) {
        return this.getClass().getClassLoader().getResourceAsStream(filename);
    }

}