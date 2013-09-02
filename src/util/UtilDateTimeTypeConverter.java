/**
 * Claroline Mobile - Android
 * 
 * @package     util
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package util;

import java.lang.reflect.Type;

import org.joda.time.DateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Claroline Mobile - Android
 * 
 * DateTime(De)Serializer class.
 * 
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class UtilDateTimeTypeConverter implements JsonSerializer<DateTime>,
		JsonDeserializer<DateTime> {
	@Override
	public DateTime deserialize(final JsonElement json, final Type type,
			final JsonDeserializationContext context) {
		try {
			return new DateTime(json.getAsString());
		} catch (IllegalArgumentException e) {
			return new DateTime(json.getAsString().replaceFirst(" ", "T"));
		}
	}

	@Override
	public JsonElement serialize(final DateTime src, final Type srcType,
			final JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}
}