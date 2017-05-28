package de.timmyrs.suprdiscordbot.structures;

/**
 * Reaction Structure.
 *
 * @author timmyRS
 */
public class Reaction extends Structure
{
	/**
	 * The amount of times this Emoji has been used to react.
	 */
	public int count;
	/**
	 * Has this user/bot reacted with this Emoji?
	 */
	public boolean me;
	/**
	 * The Emoji.
	 */
	public Emoji emoji;

	public Reaction[] getArray(int size)
	{
		return new Reaction[size];
	}

	public String toString()
	{
		return "{Reaction " + emoji.toString() + "}";
	}
}
