package com.aceper13.research.rules.jexl;

import com.acepero13.research.ruleengine.api.Condition;
import com.acepero13.research.ruleengine.api.Facts;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlException;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.util.Objects.requireNonNull;

public class JexlCondition implements Condition {
    private static final Logger logger = LogManager.getLogger();
    private final String condition;

    public JexlCondition(String condition) {
        this.condition = requireNonNull(condition, "Condition cannot be empty");
    }

    @Override
    public boolean evaluate(Facts facts) {
        requireNonNull(facts, "Facts cannot be empty");
        JexlExpression expr = JexlRule.DEFAULT_JEXL.createExpression(condition);
        JexlContext jc = new MapContext(facts.asMap());
        return tryToEvaluate(expr, jc);

    }

    private static boolean tryToEvaluate(JexlExpression expr, JexlContext jc) {
        try {
            return (boolean) expr.evaluate(jc);
        } catch (JexlException e) {
            logger.error("Error evaluating expression: {}. Check that the expression matches the fact, and the fact base contains the variable. Error: {}", expr, e.getMessage());
        } catch (ClassCastException e) {
            logger.error("Error evaluating expression: {}. Check that the expression evaluates to true or false. Error: {}", expr, e.getMessage());
        }
        return false;
    }
}
