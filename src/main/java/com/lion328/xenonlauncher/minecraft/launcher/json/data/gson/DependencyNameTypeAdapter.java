// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lion328.xenonlauncher.downloader.repository.DependencyName;

import java.lang.reflect.Type;

public class DependencyNameTypeAdapter implements JsonSerializer<DependencyName>, JsonDeserializer<DependencyName>
{

    @Override
    public JsonElement serialize(DependencyName dependencyName, Type type, JsonSerializationContext jsonSerializationContext)
    {
        return new JsonPrimitive(dependencyName.toString());
    }

    @Override
    public DependencyName deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        try
        {
            return new DependencyName(jsonElement.getAsString());
        }
        catch (IllegalArgumentException e)
        {
            throw new JsonParseException(e);
        }
    }
}
