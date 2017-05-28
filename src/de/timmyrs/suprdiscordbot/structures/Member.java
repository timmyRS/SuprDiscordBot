package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import java.util.ArrayList;

/**
 * Member Structure.
 * You can retrieve an array of member structures using {@link Guild#members}.
 *
 * @author timmyRS
 */
@SuppressWarnings("unused")
public class Member extends Structure
{
	/**
	 * Correlating user object
	 */
	public User user;
	/**
	 * Date the user joined the guild
	 */
	public String joined_at;
	/**
	 * Is the user deafened?
	 */
	public boolean deaf;
	/**
	 * Is the user muted?
	 */
	public boolean mute;
	/**
	 * ID of the guild this member is part of.
	 * Use {@link #getGuild()} to get {@link Guild} object tho.
	 */
	public String guild_id;
	/**
	 * Use {@link #getRoles()} to get a list of Roles this Member is a part of.
	 */
	public String[] roles;
	/**
	 * Nickname of this member.
	 * Use {@link #getName()} to safely the user's name.
	 */
	public String nick;

	/**
	 * @return Name of the member - Either nickname or username
	 */
	public String getName()
	{
		if(nick == null)
		{
			return user.username;
		}
		return nick;
	}

	/**
	 * @return {@link Guild} this member is part of
	 */
	public Guild getGuild()
	{
		return Main.discordAPI.getGuild(guild_id);
	}

	/**
	 * Assigns the given {@link Role}
	 *
	 * @param r {@link Role} object
	 * @return this
	 * @since 1.2
	 */
	public Member addRole(Role r)
	{
		DiscordAPI.request("PUT", "/guilds/" + this.guild_id + "/members/" + this.user.id + "/roles/" + r.id);
		return this;
	}

	/**
	 * Removes Member from the given {@link Role}
	 *
	 * @param r {@link Role} object
	 * @return this
	 * @since 1.2
	 */
	public Member removeRole(Role r)
	{
		DiscordAPI.request("DELETE", "/guilds/" + this.guild_id + "/members/" + this.user.id + "/roles/" + r.id);
		return this;
	}

	/**
	 * @return List of {@link Role} objects this member is part of
	 */
	public Role[] getRoles()
	{
		Guild g = getGuild();
		if(g == null)
		{
			return null;
		}
		ArrayList<Role> roles = new ArrayList<>();
		for(String rid : this.roles)
		{
			Role r = g.getRole(rid);
			if(r != null)
			{
				roles.add(r);
			}
		}
		return roles.toArray(new Role[roles.size()]);
	}

	/**
	 * @param role {@link Role} object to be tested for
	 * @return Weather this member is part of the given role
	 */
	public boolean hasRole(Role role)
	{
		for(Role r : getRoles())
		{
			if(role.equals(r))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return {@link Presence} object this member correlates to
	 */
	public Presence getPresence()
	{
		return getGuild().getPresence(user.id);
	}

	public Member[] getArray(int size)
	{
		return new Member[size];
	}

	public String toString()
	{
		return "{Member " + this.user.toString() + " (" + this.nick + ")}";
	}
}
