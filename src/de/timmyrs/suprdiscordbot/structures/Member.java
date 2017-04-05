package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.Main;

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
	 * Role IDs this member is part of.
	 * Use {@link #getRoles()} to get a list of {@link Role} objects.
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
