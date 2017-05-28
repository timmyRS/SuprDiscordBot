package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import java.util.ArrayList;

/**
 * Role Structure.
 * To get a Role object use {@link Guild#getRole(String)} or {@link Guild#getRoleByName(String)},
 * or to get an array of Roles, use {@link Guild#getRoles()} or {@link Member#getRoles()}.
 *
 * @author timmyRS
 */
public class Role extends Structure
{
	/**
	 * Role ID.
	 */
	public String id;
	/**
	 * Role name.
	 */
	public String name;
	/**
	 * Integer representation of hexadecimal color code.
	 */
	public int color;
	/**
	 * If this role is pinned in the user listing.
	 */
	public boolean hoist;
	/**
	 * Position of this role.
	 */
	public int position;
	/**
	 * Permission bit set.
	 *
	 * @see de.timmyrs.suprdiscordbot.apis.PermissionAPI#bitsToStrings(int)
	 */
	public int permissions;
	/**
	 * Is this Role managed by an Integration?
	 */
	public boolean managed;
	/**
	 * Is this Role mentionable?
	 */
	public boolean mentionable;

	/**
	 * Returns Guild this role is part of
	 *
	 * @return {@link Guild} object this Role is part of.
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
	 * Assigns Role to given {@link Member}.
	 * <p>
	 * <code>
	 * var role = member.getGuild().getRoleByName("Member");<br>
	 * if(role != null)<br>
	 * {<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;role.assign(member);<br>
	 * }<br>
	 * </code>
	 *
	 * @param m {@link Member} object
	 * @return this
	 * @since 1.2
	 */
	public Role assign(Member m)
	{
		DiscordAPI.request("PUT", "/guilds/" + m.guild_id + "/members/" + m.user.id + "/roles/" + this.id);
		return this;
	}

	/**
	 * Assigns Role to given {@link Presence}.
	 * <p>
	 * <code>
	 * var role = presence.getGuild().getRoleByName("Member");<br>
	 * if(role != null)<br>
	 * {<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;role.assign(presence);<br>
	 * }<br>
	 * </code>
	 *
	 * @param p {@link Presence} object
	 * @return this
	 * @since 1.2
	 */
	public Role assign(Presence p)
	{
		return this.assign(p.getMember());
	}

	/**
	 * Removes Role from given {@link Member}.
	 *
	 * @param m {@link Member} object
	 * @return this
	 * @since 1.2
	 */
	public Role remove(Member m)
	{
		DiscordAPI.request("DELETE", "/guilds/" + m.guild_id + "/members/" + m.user.id + "/roles/" + this.id);
		return this;
	}

	/**
	 * Removes Role from given {@link Presence}.
	 *
	 * @param p {@link Presence} object
	 * @return this
	 * @since 1.2
	 */
	public Role remove(Presence p)
	{
		return this.remove(p.getMember());
	}

	/**
	 * Returns list of Members who are part of this Role
	 *
	 * @return List of {@link Member} objects of online users with this role.
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
	 * @return Handle of this role to be used in a message.
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
}
