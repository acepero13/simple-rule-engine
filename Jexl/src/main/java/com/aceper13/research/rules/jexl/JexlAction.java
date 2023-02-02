package com.aceper13.research.rules.jexl;

import com.acepero13.research.ruleengine.api.Action;
import com.acepero13.research.ruleengine.api.Facts;
import org.apache.commons.jexl3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class JexlAction implements Action {
    private static final Logger logger = LogManager.getLogger();
    private final String action;
    private final JexlEngine jexlEngine;

    public JexlAction(String action, JexlEngine jexlEngine) {
        this.action = Objects.requireNonNull(action, "action cannot be empty");
        this.jexlEngine = Objects.requireNonNull(jexlEngine, "Engine cannot be empty");
    }

    public JexlAction(String action) {
        this(action, JexlRule.DEFAULT_JEXL);
    }


    @Override
    public void execute(Facts facts) {
        Objects.requireNonNull(facts, "Facts cannot be empty");
        JexlScript script = this.jexlEngine.createScript(action);
        JexlContext ctx = new MapContext(facts.asMap());
        ctx.set("facts", new FactsSetterScript(facts));
        this.tryToEvaluate(script, ctx, facts);
    }

    private void tryToEvaluate(JexlScript expr, JexlContext ctx, Facts facts) {
        try {
            expr.execute(ctx);
        } catch (JexlException e) {
            logger.error("Error executing action: {} on facts: {}", action, facts, e);
        } catch (Exception e) {
            logger.error("ERROR");
        }
    }

    public static class FactsSetterScript {
        private final Facts facts;

        private FactsSetterScript(Facts facts) {
            this.facts = facts;
        }

        @SuppressWarnings("unused")
        public <T> void put(String factName, T value) {
            facts.put(factName, value);
        }
    }
}
