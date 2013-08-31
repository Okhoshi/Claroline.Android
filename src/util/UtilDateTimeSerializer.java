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

import org.joda.time.DateTime;

import com.activeandroid.serializer.TypeSerializer;

/**
 * Claroline Mobile - Android
 * 
 * Serialize the Joda's {@link DateTime} type for ActiveAndroid record.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class UtilDateTimeSerializer extends TypeSerializer {

	@Override
	public Object deserialize(final Object data) {
		if (data == null) {
			return DateTime.now();
		}

		return new DateTime(((Long) data).longValue());
	}

	@Override
	public Class<?> getDeserializedType() {
		return DateTime.class;
	}

	@Override
	public Class<?> getSerializedType() {
		return Long.class;
	}

	@Override
	public Object serialize(final Object data) {
		if (data == null) {
			return null;
		}

		return ((DateTime) data).getMillis();
	}
}
