package de.timmyrs.suprdiscordbot.structures;

/**
 * Role Structure
 *
 * @author timmyRS
 */
public class Role extends Structure
{
	/**
	 * Role ID
	 */
	public String id;
	/**
	 * Role name
	 */
	public String name;
	/**
	 * Integer representation of hexadecimal color code
	 */
	public int color;
	/**
	 * If this role is pinned in the user listing
	 */
	public boolean hoist;
	/**
	 * Position of this role
	 */
	public int position;
	/**
	 * Permission bit set
	 */
	public int permissions;
	/**
	 * Whether this role is managed by an integration
	 */
	public boolean managed;
	/**
	 * Whether this role is mentionable
	 */
	public boolean mentionable;

	public String getHandle()
	{
		return "<&" + id + ">";
	}

	public Role[] getArray(int size)
	{
		return new Role[size];
	}

	public String toString()
	{
		return "{Role " + this.name + " #" + this.id + "}";
	}
}
