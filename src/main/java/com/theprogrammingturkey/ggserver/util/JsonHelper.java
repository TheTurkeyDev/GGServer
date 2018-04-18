package com.theprogrammingturkey.ggserver.util;

import java.util.Map;

import org.json.simple.JSONValue;

import com.google.gson.JsonElement;

public class JsonHelper
{
	@SuppressWarnings("unchecked")
	public static Map<String, Object> gsonToSimpleObject(JsonElement json)
	{
		return (Map<String, Object>) JSONValue.parse(json.toString());
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> gsonToSimpleString(JsonElement json)
	{
		return (Map<String, String>) JSONValue.parse(json.toString());
	}
}
