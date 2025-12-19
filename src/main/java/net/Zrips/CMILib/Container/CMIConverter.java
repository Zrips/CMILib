package net.Zrips.CMILib.Container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Utility class for converting different object types.
 */
public final class CMIConverter {

	/**
	 * Creates a new instance of this class.
	 * <p>
	 * The constructor is private because this is a utility class.
	 */
	private CMIConverter() {
	}

	/**
	 * Converts the elements of the given {@link Iterable} into a new type using the provided {@link Function}.
	 *
	 * @param <O>       the type of elements in the input iterable.
	 * @param <N>       the type of elements in the output list.
	 * @param iterable  the input {@link Iterable} containing the elements to be converted.
	 * @param converter the {@link Function} used to convert the elements.
	 * @return a {@link List} containing the converted elements.
	 */
	public static <O, N> List<N> convert(final Iterable<O> iterable, final Function<O, N> converter) {
		final List<N> converted = new ArrayList<>();

		for (final O old : iterable) {
			final N result = converter.apply(old);

			if (result != null)
				converted.add(result);
		}

		return converted;
	}

	/**
	 * Converts the elements of the given {@link Iterable} into a new type using the provided {@link Function}.
	 *
	 * @param <O>       the type of elements in the input iterable.
	 * @param <N>       the type of elements in the output set.
	 * @param iterable  the input {@link Iterable} containing the elements to be converted.
	 * @param converter the {@link Function} used to convert the elements.
	 * @return a {@link Set} containing the converted elements.
	 */
	public static <O, N> Set<N> convertSet(final Iterable<O> iterable, final Function<O, N> converter) {
		final Set<N> converted = new HashSet<>();

		for (final O old : iterable) {
			final N result = converter.apply(old);

			if (result != null)
				converted.add(result);
		}

		return converted;
	}

	/**
	 * Converts an array of elements into a {@link List} of a new type using the provided {@link Function}.
	 *
	 * @param <O>       the type of elements in the input array.
	 * @param <N>       the type of elements in the output list.
	 * @param array     the input array containing the elements to be converted.
	 * @param converter the {@link Function} used to convert the elements.
	 * @return a {@link List} containing the converted elements.
	 */
	public static <O, N> List<N> convert(final O[] array, final Function<O, N> converter) {
		final List<N> converted = new ArrayList<>();

		for (final O old : array)
			converted.add(converter.apply(old));

		return converted;
	}
}
