package de.timmyrs.suprdiscordbot.structures;

import com.sun.istack.internal.NotNull;

/**
 * Abstract template class for all of the API Structures
 *
 * @author timmyRS
 */
public abstract class Structure
{
	/**
	 * Get Array
	 *
	 * @param size Size of the array to be created
	 * @return New array of Structure with specified size
	 */
	@NotNull
	public abstract Structure[] getArray(final int size);

	/**
	 * To String
	 *
	 * @return String representation of Structure
	 */
	@NotNull
	public abstract String toString();

	/**
	 * Equals
	 *
	 * @param o Other Structure
	 * @return Weather the other structure is equal to this structure
	 * @since 1.2
	 */
	@NotNull
	public boolean equals(Structure o)
	{
		return o != null && o.toString().equals(this.toString());
	}
}
