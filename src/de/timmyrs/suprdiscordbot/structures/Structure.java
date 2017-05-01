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
	 * @param size Size of the array to be created
	 * @return New array of Structure with specified size
	 */
	@NotNull
	public abstract Structure[] getArray(final int size);

	/**
	 * @return String representation of Structure
	 */
	@NotNull
	public abstract String toString();

/*
	 * @param o Other Structure
	 * @return Weather the other structure is equal to this structure
	 * @since 1.2
	 */
	//public abstract boolean equals(Structure o);
}
