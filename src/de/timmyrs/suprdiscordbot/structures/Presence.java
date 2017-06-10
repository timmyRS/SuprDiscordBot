package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.Main;

/**
 * Presence Structure.
 * You can retrieve a Presence Structure using {@link Guild#getPresence(Member)}.
 *
 * @author timmyRS
 */
@SuppressWarnings("unused")
public class Presence extends Structure
{
	/**
	 * The {@link User} object.
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
	 * The status - Either "online", "idle", "dnd" or "offline".
	 */
	public String status;

	/**
	 * @return Guild this presence is part of.
	 */
	public Guild getGuild()
	{
		if(guild_id == null)
		{
			return null;
		}
		return Main.discordAPI.getGuild(guild_id);
	}

	/**
	 * Gets the DM channel with this Member.
	 * <p>
	 * <code>
	 * presence.getDMChannel().sendMessage("Hello, world!");
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
	 * presence.sendDM("Hello, world!");
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
	 * @return {@link Member} object correlating with this presence.
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
