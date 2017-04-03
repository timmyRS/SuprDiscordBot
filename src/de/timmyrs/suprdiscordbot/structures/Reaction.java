package de.timmyrs.suprdiscordbot.structures;

/**
 * Reaction Structure
 *
 * @author timmyRS
 */
public class Reaction extends Structure
{
	/**
	 * Times this emoji has been used to react
	 */
	public int count;
	/**
	 * Whether this user reacted using this emoji
	 */
	public boolean me;
	/**
	 * Emoji
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
