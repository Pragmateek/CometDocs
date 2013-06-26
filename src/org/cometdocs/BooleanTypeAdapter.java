package org.cometdocs;

import java.lang.reflect.Type;
import com.google.gson.*;

/**
 * Deserialize numeric booleans, 0 and 1, because Gson does not handle them natively. 
 */
class BooleanTypeAdapter implements JsonDeserializer<Boolean>
{
	  public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	  {
		  int code = json.getAsInt();
		  return code == 0 ? false :
			  	 code == 1 ? true :
			     null;
	  }
}
