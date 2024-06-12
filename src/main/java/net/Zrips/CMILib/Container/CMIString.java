package net.Zrips.CMILib.Container;

import net.Zrips.CMILib.Colors.CMIChatColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * A simple utility class for {@link String}s.
 */
public final class CMIString {

	/**
	 * Creates a new instance of this class.
	 * <p>
	 * The constructor is private because this is a utility class.
	 */
	private CMIString() {
	}

	/**
	 * Joins elements from the input {@link String} array using spaces within a specific range.
	 *
	 * @param from  the index to start joining from.
	 * @param array the input {@link String} array.
	 * @return a {@link String} containing the joined elements within the specified range.
	 */
	public static String joinRange(final int from, final String[] array) {
		return joinRange(from, array.length, array);
	}

	/**
	 * Joins elements from the input {@link String} array using spaces within a specific range.
	 *
	 * @param from  the index to start joining from.
	 * @param to    the index to join until.
	 * @param array the input {@link String} array.
	 * @return a {@link String} containing the joined elements within the specified range.
	 */
	public static String joinRange(final int from, final int to, final String[] array) {
		return joinRange(from, to, array, " ");
	}

	/**
	 * Joins elements from the input {@link String} array using a specified delimiter within a specific range.
	 *
	 * @param from      the index to start joining from.
	 * @param to        the index to join until.
	 * @param array     the input {@link String} array.
	 * @param delimiter the delimiter used to separate the joined elements.
	 * @return a {@link String} containing the joined elements within the specified range, separated by the given
	 * delimiter.
	 */
	public static String joinRange(final int from, final int to, final String[] array, final String delimiter) {
		final StringBuilder joined = new StringBuilder();

		for (int i = from; i < CMINumber.clamp(to, 0, array.length); i++)
			joined.append((joined.length() == 0) ? "" : delimiter).append(array[i]);

		return joined.toString();
	}

	/**
	 * Joins elements from the input array, separated by ", ". We invoke {@link T#toString()} for each element given it
	 * is not {@code null}, or return an empty {@link String} if it is.
	 *
	 * @param array the input array.
	 * @param <T>   the type of elements in the array.
	 * @return a {@link String} containing the joined elements of the array.
	 */
	public static <T> String join(final T[] array) {
		return array == null ? "null" : join(Arrays.asList(array));
	}

	/**
	 * Joins elements from the input {@link Iterable array}, separated by ", ". We invoke {@link T#toString()} for each
	 * element given it is not {@code null}, or return an empty {@link String} if it is.
	 *
	 * @param iterable the input {@link Iterable}.
	 * @param <T>      the type of elements in the iterable.
	 * @return a {@link String} containing the joined elements of the {@link Iterable}.
	 */
	public static <T> String join(final Iterable<T> iterable) {
		return iterable == null ? "null" : join(iterable, ", ");
	}

	/**
	 * Joins elements from the input array, separated by the specified delimiter. We invoke {@link T#toString()} for
	 * each element given it is not {@code null}, or return an empty {@link String} if it is.
	 *
	 * @param array     the input array.
	 * @param delimiter the delimiter used to separate the joined elements.
	 * @param <T>       the type of elements in the array.
	 * @return a {@link String} containing the joined elements of the array with the specified delimiter.
	 */
	public static <T> String join(final T[] array, final String delimiter) {
		return join(array, delimiter, object -> object == null ? "" : simplify(object));
	}

	/**
	 * Joins elements from the input {@link Iterable}, separated by the specified delimiter. We invoke
	 * {@link T#toString()} for each element given it is not {@code null}, or return an empty {@link String} if it is.
	 *
	 * @param iterable  the input {@link Iterable}.
	 * @param delimiter the delimiter used to separate the joined elements.
	 * @param <T>       the type of elements in the iterable.
	 * @return a {@link String} containing the joined elements of the {@link Iterable} with the specified
	 * delimiter.
	 */
	public static <T> String join(final Iterable<T> iterable, final String delimiter) {
		return join(iterable, delimiter, object -> object == null ? "" : simplify(object));
	}

	/**
	 * Joins elements from the input array, separated by ", ". We use the provided {@link Function} to convert each
	 * element to its {@link String} representation.
	 *
	 * @param array    the input array.
	 * @param toString the {@link Function} used to convert each element to its String representation.
	 * @param <T>      the type of elements in the array.
	 * @return a {@link String} containing the joined elements of the array with the specified delimiter.
	 */
	public static <T> String join(final T[] array, final Function<T, String> toString) {
		return join(array, ", ", toString);
	}

	/**
	 * Joins elements from the input array, separated by the specified delimiter. We use the provided {@link Function}
	 * to convert each element to its {@link String} representation.
	 *
	 * @param array     the input array.
	 * @param delimiter the delimiter used to separate the joined elements.
	 * @param toString  the {@link Function} used to convert each element to its String representation.
	 * @param <T>       the type of elements in the array.
	 * @return a {@link String} containing the joined elements of the array with the specified delimiter.
	 */
	public static <T> String join(final T[] array, final String delimiter, final Function<T, String> toString) {
		return join(Arrays.asList(array), delimiter, toString);
	}

	/**
	 * Joins elements from the input {@link Iterable}, separated by ", ". We use the provided {@link Function} to
	 * convert each element to its {@link String} representation.
	 *
	 * @param iterable the input {@link Iterable}.
	 * @param toString the {@link Function} used to convert each element to its String representation.
	 * @param <T>      the type of elements in the iterable.
	 * @return a {@link String} containing the joined elements of the {@link Iterable} with the specified
	 * delimiter.
	 */
	public static <T> String join(final Iterable<T> iterable, final Function<T, String> toString) {
		return join(iterable, ", ", toString);
	}

	/**
	 * Joins elements from the input {@link Iterable array}, separated by the specified delimiter. We use the provided
	 * {@link Function} to convert each element to its {@link String} representation.
	 *
	 * @param iterable  the input {@link Iterable}.
	 * @param delimiter the delimiter used to separate the joined elements.
	 * @param toString  the {@link Function} used to convert each element to its String representation.
	 * @param <T>       the type of elements in the iterable.
	 * @return a {@link String} containing the joined elements of the {@link Iterable} with the specified
	 * delimiter.
	 */
	public static <T> String join(final Iterable<T> iterable, final String delimiter, final Function<T, String> toString) {
		final StringBuilder message = new StringBuilder();

		for (final Iterator<T> iterator = iterable.iterator(); iterator.hasNext(); ) {
			final T next = iterator.next();

			if (next != null)
				message.append(toString.apply(next)).append(iterator.hasNext() ? delimiter : "");
		}

		return message.toString();
	}

	/**
	 * Converts some common classes such as {@link Entity} to its name automatically.
	 *
	 * @param object the {@link Object} to simplify.
	 * @return the {@link String} representation of the given {@link Object}.
	 */
	public static String simplify(final Object object) {
		if (object instanceof Entity)
			return ((Entity) object).getName();

		if (object instanceof CommandSender)
			return ((CommandSender) object).getName();

		if (object instanceof World)
			return ((World) object).getName();

		if (object instanceof Location)
			return CMILocation.toString((Location) object);

		if (object instanceof Collection)
			return join((Collection<?>) object, ", ", CMIString::simplify);

		if (object instanceof ChatColor)
			return ((Enum<?>) object).name().toLowerCase();

		if (object instanceof CMIChatColor)
			return ((CMIChatColor) object).getName();

		if (object instanceof Enum)
			return object.toString().toLowerCase();

		try {
			if (object instanceof net.md_5.bungee.api.ChatColor)
				return ((net.md_5.bungee.api.ChatColor) object).getName();
		} catch (final Exception ignored) {
		}

		return object.toString();
	}
}
