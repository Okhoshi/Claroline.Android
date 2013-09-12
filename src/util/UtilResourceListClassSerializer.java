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

import model.ResourceModel;

import com.activeandroid.serializer.TypeSerializer;

/**
 * Claroline Mobile - Android
 * 
 * Serialize the {@link Class} type for ActiveAndroid record.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class UtilResourceListClassSerializer extends TypeSerializer {

	@Override
	public Object deserialize(final Object data) {
		if (data == null) {
			return ResourceModel.class;
		}

		try {
			return Class.forName(data.toString());
		} catch (ClassNotFoundException e) {
			return ResourceModel.class;
		}
	}

	@Override
	public Class<?> getDeserializedType() {
		return Class.class;
	}

	@Override
	public Class<?> getSerializedType() {
		return String.class;
	}

	@Override
	public Object serialize(final Object data) {
		if (data == null) {
			return null;
		}

		return ((Class<?>) data).getName();
	}
}
