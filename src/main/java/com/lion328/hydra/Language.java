// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.hydra;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Language
{

    private static Map table;

    public static String get(String key)
    {
        initializeTable();

        String ret = (String) table.get(key);

        if (ret != null)
        {
            return ret;
        }

        return key;
    }

    public static void extend(Map<String, String> map)
    {
        initializeTable();

        table.putAll(map);
    }

    public static void initializeTable()
    {
        if (table == null)
        {
            table = new Gson().fromJson(
                    new InputStreamReader(Language.class.getResourceAsStream("/com/lion328/hydra/resources/lang.json"),
                            StandardCharsets.UTF_8), Map.class);
        }
    }
}
