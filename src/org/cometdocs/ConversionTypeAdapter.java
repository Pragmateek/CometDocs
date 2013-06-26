package org.cometdocs;

import java.lang.reflect.Type;

import com.google.gson.*;

class ConversionTypeAdapter implements JsonDeserializer<ConversionType>
{
	  public ConversionType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	  {
		  String type = json.getAsString();
		  
		  String[] tokens = type.split("2");
		  
		  return new ConversionType(tokens[0], tokens[1]);
	  }
}
