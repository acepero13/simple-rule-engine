package com.aceper13.research.rules.jexl;

import com.acepero13.research.ruleengine.api.Rule;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// TODO: Refactor this
public class JexlRuleBuilder {
    private final String name;
    private final String description;
    private final int priority;
    private final String condition;
    private final List<String> actions = new ArrayList<>();

    private JexlRuleBuilder(Builder builder) {
        name = builder.name;
        description = builder.description;
        priority = builder.priority;
        condition = builder.condition;
        actions.add(builder.action);
    }

    public static Rule of(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream, "input stream cannot be null");
        var mapper = new ObjectMapper(new YAMLFactory());
        Builder builder = mapper.readValue(inputStream, Builder.class);
        return builder.build();
    }


    public static final class Builder {
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;
        @JsonProperty("priority")
        private int priority;
        @JsonProperty("condition")
        private String condition;
        @JsonProperty("actions")
        private String action;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder priority(int val) {
            priority = val;
            return this;
        }

        public Builder when(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder then(String action) {
            this.action = action;
            return this;
        }

        public Rule build() {
            return new JexlRule(name, description, priority, new JexlCondition(condition), new JexlAction(action));
        }
    }
}
