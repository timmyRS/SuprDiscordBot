package de.timmyrs.suprdiscordbot.structures;

/**
 * Overwrite Structure
 *
 * @author timmyRS
 */
public class Overwrite extends Structure
{
	/**
	 * Role or user ID
	 */
	public String id;
	/**
	 * Either "role" or "member"
	 */
	public String type;
	/**
	 * Permission bit set
	 */
	public int allow;
	/**
	 * Permission bit set
	 */
	public int deny;

	public Overwrite[] getArray(int size)
	{
		return new Overwrite[size];
	}

	public String toString()
	{
		return "{Overwrite " + this.type + " #" + this.id + "}";
	}
}
