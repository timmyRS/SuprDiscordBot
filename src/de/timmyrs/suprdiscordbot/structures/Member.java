package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.Main;

/**
 * Member Structure.
 * You can retrieve an array of member structures using {@link Guild#members}.
 *
 * @author timmyRS
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Member extends Structure
{
	/**
	 * Correlating user object.
	 */
	public User user;
	/**
	 * Date the user joined the guild.
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
	 * ID of the Guild this Member is part of.
	 * Use {@link Member#getGuild()} to get the {@link Guild} object.
	 */
	public String guild_id;
	/**
	 * Use {@link Member#getRoles()} to get an array of {@link Role} objects this member is a part of.
	 */
	public String[] roles;
	/**
	 * Nickname of this Member.
	 * Use {@link Member#getName()} to get the user's name.
	 */
	public String nick;

	/**
	 * @return The Nickname or - if not set - the Username of this Member.
	 */
	public String getName()
	{
		return (nick == null ? user.username : nick);
	}

	/**
	 * @return {@link Guild} this member is part of.
	 */
	public Guild getGuild()
	{
		return Main.discordAPI.getGuild(guild_id);
	}

	/**
	 * @return List of IDs of Roles the Member is part of.
	 * @see Member#getRoles()
	 */
	public String[] getRoleIDs()
	{
		return this.roles;
	}

	/**
	 * @return List of {@link Role} objects the Member is part of.
	 */
	public Role[] getRoles()
	{
		Guild g = getGuild();
		Role[] roleArr = new Role[this.roles.length];
		int i = 0;
		for(String rid : this.roles)
		{
			Role r = g.getRole(rid);
			roleArr[i++] = r;
		}
		return roleArr;
	}

	/**
	 * @param role {@link Role} object to be tested for
	 * @return Weather this member is part of the given role.
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
	 * @param name Name of the Role to be tested for
	 * @return Weather this member is part of the given role.
	 * @since 1.2
	 */
	public boolean hasRole(String name)
	{
		for(Role r : getRoles())
		{
			if(r.name.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the DM channel with this Member.
	 * <p>
	 * <code>
	 * msg.getAuthor().getMember(msg.getGuild()).getDMChannel().sendMessage("Hello, world!");
	 * </code>
	 *
	 * @return {@link Channel} object of DM Channel.
	 * @since 1.2
	 */
	public Channel getDMChannel()
	{
		return this.user.getDMChannel();
	}

	/**
	 * Sends a DM to the user.
	 * <p>
	 * <code>
	 * msg.getAuthor().getMember(msg.getGuild()).sendDM("Hello, world!");
	 * </code>
	 *
	 * @param content Content of the message
	 * @return {@link Message} object of the newly sent message.
	 * @since 1.2
	 */
	public Message sendDM(String content)
	{
		return this.user.sendDM(content);
	}

	/**
	 * @return {@link Presence} object this member correlates to.
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
		return "{Member " + this.user.toString() + " \"" + this.getName() + "\"}";
	}
}
