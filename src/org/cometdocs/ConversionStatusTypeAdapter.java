package org.cometdocs;

import java.lang.reflect.Type;
import com.google.gson.*;

class ConversionStatusTypeAdapter implements JsonDeserializer<ConversionStatus>
{
	  public ConversionStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	  {
		  int code = json.getAsInt();
		  ConversionStatus[] values = ConversionStatus.values();
		  return code < values.length ? values[code] : null;
	  }
}
