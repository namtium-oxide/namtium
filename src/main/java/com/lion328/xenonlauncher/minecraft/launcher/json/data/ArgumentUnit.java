package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.lion328.xenonlauncher.minecraft.StartupConfiguration;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ArgumentUnit
{

    private List<String> value;
    private List<Rule> rules;

    public ArgumentUnit()
    {

    }

    public ArgumentUnit(List<String> value, List<Rule> rules)
    {
        this.value = value;
        this.rules = rules;
    }

    public List<String> getValue()
    {
        return Collections.unmodifiableList(value);
    }

    public List<Rule> getRules()
    {
        return Collections.unmodifiableList(rules);
    }

    public List<String> getValueOnConfig(StartupConfiguration config)
    {
        if (!Rule.isAllowed(rules, config))
        {
            return Collections.emptyList();
        }

        return getValue();
    }

    public static class Deserializer implements JsonDeserializer<ArgumentUnit>
    {

        @Override
        public ArgumentUnit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            if (json.isJsonPrimitive())
            {
                return new ArgumentUnit(Collections.singletonList(json.getAsString()), null);
            }

            JsonObject obj = json.getAsJsonObject();

            List<String> value;
            JsonElement valueElem = obj.get("value");
            if (valueElem.isJsonArray())
            {
                Type type = new TypeToken<List<String>>() {}.getType();
                value = context.deserialize(valueElem, type);
            }
            else
            {
                value = Collections.singletonList(valueElem.getAsString());
            }

            Type rulesType = new TypeToken<List<Rule>>() {}.getType();
            List<Rule> rules = context.deserialize(obj.get("rules"), rulesType);

            return new ArgumentUnit(value, rules);
        }
    }
}
