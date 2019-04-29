package quadratix;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;

public class ListUtil {
	
	/**
	 * Pick randomly an item in the given collection.
	 * @param collection The collection.
	 * @param <T> The generic type of the collection.
	 * @return Return the randomly picked up element from `collection`.
	 */
	@Nullable
	@Contract("null -> fail")
	public static <T> T pickRandomly(@NotNull Collection<T> collection) {
		// Code Inspired from https://stackoverflow.com/a/124693
		int index = new Random().nextInt(collection.size());
		int i = 0;
		
		for (T t : collection) {
			if (i == index)
				return t;
			
			i++;
		}
		
		return null;
	}
}
