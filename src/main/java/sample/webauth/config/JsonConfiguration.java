package sample.webauth.config;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

/**
 * Helper method to read json configuration files 
 *
 */
public class JsonConfiguration
{
	/*
		Apply a function to each json object read from a json configuration file
	*/
	public static void consumeObjectArrayFromFile(String filePath, Consumer<JsonObject> consumer) throws Exception {

		try (InputStream is = new FileInputStream(filePath);
			JsonReader reader = Json.createReader(is)
		) {
			JsonArray array = reader.readArray();
			for (JsonObject item : array.getValuesAs(JsonObject.class)) {
				consumer.accept(item);
			}
		}
	}

	/*
		Get a list of strings from a json string array
	*/
	public static List<String> toStringList(JsonArray array) {
		return array.getValuesAs(JsonString.class).stream()
			.map(x -> x.toString())
			.collect(Collectors.toCollection(ArrayList::new));
	}

}	
