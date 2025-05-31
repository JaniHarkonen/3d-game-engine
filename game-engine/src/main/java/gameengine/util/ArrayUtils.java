package gameengine.util;

public final class ArrayUtils {

	public static boolean isInBounds(int index, Object[] array) {
		return (index >= 0 && index <= array.length);
	}
}
