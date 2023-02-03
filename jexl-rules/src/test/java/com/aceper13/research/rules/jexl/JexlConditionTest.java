package com.aceper13.research.rules.jexl;

import com.acepero13.research.ruleengine.api.Facts;
import com.acepero13.research.ruleengine.model.InMemoryFacts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JexlConditionTest {

    @Test
    void evaluatesToTrue(){

        Facts facts = new InMemoryFacts();
        facts.put("value", 10);
        var condition = new JexlCondition("value > 5");

        assertTrue(condition.evaluate(facts));
    }

    @Test
    void evaluatesToFalse(){

        Facts facts = new InMemoryFacts();
        facts.put("value", 1);
        var condition = new JexlCondition("value > 5");

        assertFalse(condition.evaluate(facts));
    }

    @Test
    void evaluatesToFalseBecauseValueDoesNotExists(){

        Facts facts = new InMemoryFacts();
        var condition = new JexlCondition("value > 5");

        assertFalse(condition.evaluate(facts));
    }

    @Test
    void evaluatesToFalseBecauseCannotCast(){

        Facts facts = new InMemoryFacts();
        facts.put("value", 1);
        var condition = new JexlCondition("value + 5");

        assertFalse(condition.evaluate(facts));
    }
}