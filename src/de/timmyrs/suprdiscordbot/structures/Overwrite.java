package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

/**
 * Overwrite Structure.
 * You can retrieve an array of overwrite strucutures using {@link Channel#permission_overwrites}
 * and can get an empty embed structure using {@link DiscordAPI#createOverwrite()}.
 *
 * @author timmyRS
 * @see Channel#overwritePermissions(Overwrite)
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

	/**
	 * @param u User to overwrite permissions of
	 * @return this
	 * @since 1.1
	 */
	public Overwrite setUser(User u)
	{
		this.type = "member";
		this.id = u.id;
		return this;
	}

	/**
	 * @param r Role to overwrite permissions of
	 * @return this
	 * @since 1.1
	 */
	public Overwrite setRole(Role r)
	{
		this.type = "role";
		this.id = r.id;
		return this;
	}

	/**
	 * @param allow Bitwise value of permission to allow
	 * @return this
	 * @since 1.1
	 */
	public Overwrite allow(int allow)
	{
		this.allow += allow;
		return this;
	}

	/**
	 * @param deny Bitwise value of permission to disallow
	 * @return this
	 * @since 1.1
	 */
	public Overwrite deny(int deny)
	{
		this.deny += deny;
		return this;
	}

	public Overwrite[] getArray(int size)
	{
		return new Overwrite[size];
	}

	public String toString()
	{
		return "{Overwrite for " + this.type + " #" + this.id + "}";
	}

	public boolean equals(Overwrite o)
	{
		return o.id.equals(this.id);
	}
}
