package de.timmyrs.suprdiscordbot.structures;

/**
 * @author timmyRS
 */
public abstract class Structure
{
	/**
	 * @param size Size of the array to be created
	 * @return New array of Structure with given size.
	 */
	public abstract Structure[] getArray(final int size);

	/**
	 * @return String representation of Structure.
	 */
	public abstract String toString();

	/**
	 * @param o Other Structure
	 * @return Weather the other structure is equal to this structure.
	 * @since 1.2
	 */
	public boolean equals(Structure o)
	{
		return o != null && o.toString().equals(this.toString());
	}
}
