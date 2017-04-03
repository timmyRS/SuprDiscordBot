package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.Main;

/**
 * VoiceState Structure
 *
 * @author timmyRS
 */
public class VoiceState extends Structure
{
	/**
	 * ID of the guild this VoiceState belongs to.
	 * Use {@link #getGuild()} to get the {@link Guild} tho.
	 */
	public String guild_id;
	/**
	 * The session ID this VoiceState is for
	 */
	public String session_id;
	/**
	 * Whether this user is deafened by the server
	 */
	public boolean deaf;
	/**
	 * Whether this user is muted by the server
	 */
	public boolean mute;
	/**
	 * Whether this user locally deafened
	 */
	public boolean self_deaf;
	/**
	 * Whether this user locally muted
	 */
	public boolean self_mute;
	private String channel_id;
	private String user_id;

	/**
	 * @return The {@link Channel} is VoiceState is part of
	 */
	public Channel getChannel()
	{
		return Main.discordAPI.getChannel(channel_id);
	}

	/**
	 * @return The {@link Guild} this VoiceState is part of
	 */
	public Guild getGuild()
	{
		return Main.discordAPI.getGuild(guild_id);
	}

	/**
	 * @return The {@link User} this VoiceState belongs to
	 */
	public User getUser()
	{
		return Main.discordAPI.getUser(user_id);
	}

	public VoiceState[] getArray(int size)
	{
		return new VoiceState[size];
	}

	public String toString()
	{
		return "{VoiceState for user #" + user_id + " in channel #" + channel_id + "}";
	}
}
