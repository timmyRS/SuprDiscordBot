package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.Main;

import java.util.ArrayList;

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
	 * Permission bit set.
	 *
	 * @see de.timmyrs.suprdiscordbot.apis.PermissionAPI#bitsToStrings(int)
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

	/**
	 * Returns Guild this role is part of
	 *
	 * @return {@link Guild} object this Role is part of
	 * @since 1.2
	 */
	public Guild getGuild()
	{
		for(Guild g : Main.discordAPI.getGuilds())
		{
			for(Role r : g.roles)
			{
				if(r.equals(this))
				{
					return g;
				}
			}
		}
		return null;
	}

	/**
	 * Returns list of online users with this role
	 *
	 * @return List of {@link Member} objects of online users with this role
	 * @since 1.2
	 */
	public Member[] getMembers()
	{
		ArrayList<Member> arrayList = new ArrayList<>();
		for(Member m : getGuild().members)
		{
			if(m.hasRole(this))
			{
				arrayList.add(m);
			}
		}
		Member[] tmp = new Member[arrayList.size()];
		return arrayList.toArray(tmp);
	}

	/**
	 * @return Handle of this role to be used in a message
	 */
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
		return "{Role \"" + this.name + "\" #" + this.id + "}";
	}

	public boolean equals(Role o)
	{
		return o.id.equals(this.id);
	}
}
