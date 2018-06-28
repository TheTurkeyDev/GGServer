package com.theprogrammingturkey.ggserver.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonHelper
{
	private static final JsonParser PARSER = new JsonParser();
	private static final ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	public static Map<String, Object> gsonToSimpleObject(JsonElement json)
	{
		try
		{
			return mapper.readValue(json.toString(), HashMap.class);
		} catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static JsonElement getJsonFromMap(Map<String, String> jsonMap)
	{
		return PARSER.parse(jsonMap.toString());
	}

	public static JsonElement getJsonFromString(String json)
	{
		return PARSER.parse(json);
	}
}
