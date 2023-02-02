package com.aceper13.research.rules.jexl;

import com.acepero13.research.ruleengine.api.Action;
import com.acepero13.research.ruleengine.api.Condition;
import com.acepero13.research.ruleengine.api.Facts;
import com.acepero13.research.ruleengine.model.rules.BasicRule;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;

import java.util.List;
import java.util.Objects;

public class JexlRule extends BasicRule {
    static final JexlEngine DEFAULT_JEXL = new JexlBuilder().create();
    private final Condition condition;
    private final List<Action> actions;

    public JexlRule(String name, String description, int priority,
                    Condition condition, List<Action> actions) {
        super(name, description, priority);
        this.condition = Objects.requireNonNull(condition, "Condition cannot be empty");
        this.actions = Objects.requireNonNull(actions, "Actions cannot be empty");
    }

    public JexlRule(String name, String description, int priority, Condition condition, Action action) {
        this(name, description, priority, condition, List.of(action));
    }

    @Override
    public boolean evaluates(Facts facts) {
        Objects.requireNonNull(facts, "Facts cannot be empty");
        return condition.evaluate(facts);
    }

    @Override
    public void execute(Facts facts) throws Exception {
        Objects.requireNonNull(facts, "Facts cannot be empty");
        for (Action action : actions) {
            action.execute(facts);
        }
    }

}
