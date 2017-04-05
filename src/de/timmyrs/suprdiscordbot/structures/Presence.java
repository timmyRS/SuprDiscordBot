package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.Main;

/**
 * Presence Structure
 *
 * @author timmyRS
 */
public class Presence extends Structure
{
	/**
	 * The {@link User} object
	 */
	public User user;
	/**
	 * The {@link Game} object played or null if none.
	 */
	public Game game;
	/**
	 * ID of the guild this presences is part of.
	 * Use {@link #getGuild()} to get this presences' guild tho.
	 */
	public String guild_id;
	/**
	 * The status - Either "online", "idle", "dnd" or "offline"
	 */
	public String status;

	/**
	 * @return Guild this presence is part of
	 */
	public Guild getGuild()
	{
		return Main.discordAPI.getGuild(guild_id);
	}

	/**
	 * @return {@link Member} object correlating with this presence
	 * @since 1.1
	 */
	public Member getMember()
	{
		return getGuild().getMember(this);
	}

	public Presence[] getArray(int size)
	{
		return new Presence[size];
	}

	public String toString()
	{
		return "{Presence of " + this.user.toString() + "}";
	}
}
