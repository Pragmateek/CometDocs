package org.cometdocs;

import java.lang.reflect.Type;
import com.google.gson.*;

class StatusTypeAdapter implements JsonDeserializer<Status>
{
	  public Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	  {
		  int code = json.getAsInt();
		  Status[] values = Status.values();
		  return code < values.length ? values[code] : null;
	  }
}
