package com.aceper13.research.rules.jexl;

import com.acepero13.research.ruleengine.api.Facts;
import com.acepero13.research.ruleengine.model.InMemoryFacts;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JexlActionTest {
    private final Facts facts = new InMemoryFacts();

    @Test
    void executes() {
        Person person = new Person();
        facts.put("person", person);
        var action = new JexlAction("person.setAdult(true)");
        action.execute(facts);
        assertTrue(person.isAdult());
    }

    @Test
    void executesCustomFunction() {
        PrintStream originalStream = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Map<String, Object> namespaces = new HashMap<>();
        namespaces.put("sout", System.out);
        JexlEngine jexlEngine = new JexlBuilder()
                .namespaces(namespaces)
                .create();

        String func = "var hello = function() { sout:println(\"Hello from JEXL!\"); }; hello();";
        var printAction = new JexlAction(func, jexlEngine);
        printAction.execute(facts);

        assertEquals("Hello from JEXL!", outputStream.toString().strip());
        System.setOut(originalStream);

    }

    @Test
    void addsResultToFacts() {


        var action = new JexlAction("facts.put('value', true)");
        action.execute(facts);
        var value = facts.get("value", Boolean.class)
                .orElse(false);

        assertTrue(value);
    }
}