package de.timmyrs.suprdiscordbot.structures;

/**
 * Emoji Structure.
 * https://discordapp.com/developers/docs/resources/guild#emoji-object
 */
public class Emoji extends Structure
{
	/**
	 * Emoji ID.
	 */
	public String id;
	/**
	 * Emoji Name.
	 */
	public String name;
	/**
	 * Roles this Emoji is active for.
	 */
	public String[] roles;
	/**
	 * Must this emoji be wrapped in colons?
	 */
	public boolean require_colons;
	/**
	 * Is this emoji managed?
	 */
	public boolean managed;

	public Emoji[] getArray(int size)
	{
		return new Emoji[size];
	}

	public String toString()
	{
		return "{Emoji " + this.name + " #" + this.id + "}";
	}
}
