package de.timmyrs.suprdiscordbot.structures;

import com.google.gson.JsonObject;
import de.timmyrs.suprdiscordbot.Configuration;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Guild Structure.
 * You can retrieve an array of guild structures using {@link DiscordAPI#getGuilds()}.
 *
 * @author timmyRS
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Guild extends Structure
{
	/**
	 * Guild ID.
	 */
	public String id;
	/**
	 * Guild Name.
	 * 2-100 chars.
	 */
	public String name;
	/**
	 * Icon Hash.
	 *
	 * @see Guild#getIconURL()
	 */
	public String icon;
	/**
	 * Splash Hash.
	 *
	 * @see Guild#getSplashURL()
	 */
	public String splash;
	/**
	 * Voice Region.
	 */
	public String region;
	/**
	 * AFK timeout in seconds.
	 */
	public int afk_timeout;
	/**
	 * Is this guild embeddable?
	 */
	public boolean embed_enabled;
	/**
	 * Level of verification.
	 */
	public int verification_level;
	/**
	 * Default message notifications level.
	 */
	public int default_message_notifications;
	/**
	 * An array of {@link Role} objects this guild has.
	 *
	 * @see Guild#getRoles()
	 */
	public Role[] roles;
	/**
	 * Array of {@link Emoji} objects.
	 */
	public Emoji[] emojis;
	/**
	 * Array of guild features.
	 */
	public String[] features;
	/**
	 * Required Multi Factor Authentication (MFA) level for the guild.
	 */
	public int mfa_level;
	/**
	 * Date the guild was joined at.
	 * Use {@link Guild#getJoinedAt()}
	 */
	public String joined_at;
	/**
	 * Is the Guild unavailable?
	 *
	 * @see Guild#isAvailable()
	 */
	public boolean unavailable;
	/**
	 * Total number of members in this guild .
	 */
	public int member_count;
	/**
	 * Array of {@link VoiceState} objects.
	 */
	public VoiceState[] voice_states;
	/**
	 * Array of {@link Member} objects.
	 */
	public Member[] members;
	/**
	 * Array of {@link Presence} objects.
	 */
	public Presence[] presences;
	private String owner_id;
	private String afk_channel_id;
	private String embed_channel_id;
	private Channel[] channels;

	/**
	 * @return {@link Configuration}.
	 */
	public Configuration getValues()
	{
		return Main.getValuesConfig("g" + this.id);
	}

	/**
	 * @return Guild Icon URL or null if has none.
	 */
	public String getIconURL()
	{
		if(this.icon == null)
		{
			return null;
		}
		return "https://cdn.discordapp.com/icons/" + this.id + "/" + this.icon;
	}

	/**
	 * @return Guild Splash URL or null if has none.
	 */
	public String getSplashURL()
	{
		if(this.splash == null)
		{
			return null;
		}
		return "https://cdn.discordapp.com/icons/" + this.id + "/" + this.splash;
	}

	/**
	 * @return Is the Guild available?
	 * @since 1.2
	 */
	public boolean isAvailable()
	{
		return !this.unavailable;
	}

	/**
	 * @return The UNIX timestamp of when the Guild was joined at.
	 * @see Guild#getJoinedMillis()
	 * @since 1.2
	 */
	public long getJoinedAt() throws ParseException
	{
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(this.joined_at.substring(0, this.joined_at.length() - 9)).getTime() / 1000L;
	}

	/**
	 * @return The time millis of when the Guild was joined at.
	 * @see Guild#getJoinedAt()
	 * @since 1.2
	 */
	public long getJoinedMillis() throws ParseException
	{
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(this.joined_at.substring(0, this.joined_at.length() - 9)).getTime() / 1000L;
	}

	/**
	 * Changes this bot's nickname on this guild
	 *
	 * @param newnick New nickname
	 * @return this
	 */
	public Guild setNickname(final String newnick)
	{
		JsonObject json = Main.jsonParser.parse("{}").getAsJsonObject();
		json.addProperty("nick", newnick);
		DiscordAPI.request("PATCH", "/guilds/" + this.id + "/members/@me/nick", json.toString());
		return this;
	}

	/**
	 * @return An array of {@link Role} objects this guild has.
	 * @since 1.2
	 */
	public Role[] getRoles()
	{
		if(this.roles == null)
		{
			this.roles = (Role[]) DiscordAPI.request("GET", "/guilds/" + this.id + "/roles", new Role());
		}
		return this.roles;
	}

	/**
	 * @param id Role ID
	 * @return {@link Role} object with given ID or null if not found.
	 */
	public Role getRole(String id)
	{
		for(Role r : roles)
		{
			if(r.id.equals(id))
			{
				return r;
			}
		}
		return null;
	}

	/**
	 * @param name Role Name
	 * @return {@link Role} object with given name or null if not found.
	 * @since 1.2
	 */
	public Role getRoleByName(String name)
	{
		for(Role r : roles)
		{
			if(r.name.equalsIgnoreCase(name))
			{
				return r;
			}
		}
		return null;
	}

	/**
	 * @param id Member ID
	 * @return {@link Member} object with given ID or null if not found.
	 */
	public Member getMember(String id)
	{
		for(Member m : members)
		{
			if(m.user.id.equals(id))
			{
				return m;
			}
		}
		return null;
	}

	/**
	 * @param u {@link User} object
	 * @return {@link Member} Member correlating with the {@link User} object.
	 */
	public Member getMember(User u)
	{
		return getMember(u.id);
	}

	/**
	 * @param p {@link Presence} object
	 * @return {@link Member} Member correlating with the {@link Presence} object.
	 */
	public Member getMember(Presence p)
	{
		return getMember(p.user);
	}

	public void addMember(Member member)
	{
		ArrayList<Member> memberArrayList = new ArrayList<>();
		for(Member m : members)
		{
			if(!m.user.id.equals(member.user.id))
			{
				memberArrayList.add(m);
			}
		}
		memberArrayList.add(member);
		Member[] tmp = member.getArray(memberArrayList.size());
		members = memberArrayList.toArray(tmp);
	}

	public void removeMember(String id)
	{
		ArrayList<Member> memberArrayList = new ArrayList<>();
		for(Member m : members)
		{
			if(!m.user.id.equals(id))
			{
				memberArrayList.add(m);
			}
		}
		Member[] tmp = new Member[memberArrayList.size()];
		members = memberArrayList.toArray(tmp);
	}

	/**
	 * @param id User ID
	 * @return {@link Presence} object with given ID.
	 */
	public Presence getPresence(String id)
	{
		for(Presence p : presences)
		{
			if(p.user.id.equals(id))
			{
				return p;
			}
		}
		return null;
	}

	public void addPresence(Presence presence)
	{
		ArrayList<Presence> presencesArrayList = new ArrayList<>();
		for(Presence p : presences)
		{
			if(!p.equals(presence))
			{
				presencesArrayList.add(p);
			}
		}
		presencesArrayList.add(presence);
		Presence[] tmp = presence.getArray(presencesArrayList.size());
		presences = presencesArrayList.toArray(tmp);
	}

	public void removePresence(String id)
	{
		ArrayList<Presence> presencesArrayList = new ArrayList<>();
		for(Presence p : presences)
		{
			if(!p.user.id.equals(id))
			{
				presencesArrayList.add(p);
			}
		}
		Presence[] tmp = new Presence[presencesArrayList.size()];
		presences = presencesArrayList.toArray(tmp);
	}

	public void addChannel(Channel channel)
	{
		ArrayList<Channel> channelsArrayList = new ArrayList<>();
		for(Channel c : channels)
		{
			if(!c.equals(channel))
			{
				channelsArrayList.add(c);
			}
		}
		channelsArrayList.add(channel);
		Channel[] tmp = new Channel[channelsArrayList.size()];
		channels = channelsArrayList.toArray(tmp);
	}

	/**
	 * @param u {@link User} object
	 * @return {@link Presence} object correlating to the given {@link User} object.
	 */
	public Presence getPresence(User u)
	{
		return getPresence(u.id);
	}

	/**
	 * @param m {@link Member} object
	 * @return {@link Presence} object correlating to the given {@link Member} object.
	 */
	public Presence getPresence(Member m)
	{
		return getPresence(m.user.id);
	}

	/**
	 * @return List of guild channels
	 */
	public Channel[] getChannels()
	{
		if(channels == null)
		{
			channels = (Channel[]) DiscordAPI.request("/guilds/" + this.id + "/channels", new Channel());
		}
		return channels;
	}

	/**
	 * @return List of text channels which are part of this guild.
	 * @since 1.2
	 */
	public Channel[] getTextChannels()
	{
		ArrayList<Channel> channelArrayList = new ArrayList<>();
		for(Channel c : getChannels())
		{
			if(c.type.equals("text"))
			{
				channelArrayList.add(c);
			}
		}
		Channel[] tmp = new Channel[channelArrayList.size()];
		return channelArrayList.toArray(tmp);
	}

	/**
	 * @return List of voice channels which are part of this guild.
	 * @since 1.2
	 */
	public Channel[] getVoiceChannels()
	{
		ArrayList<Channel> channelArrayList = new ArrayList<>();
		for(Channel c : getChannels())
		{
			if(c.type.equals("voice"))
			{
				channelArrayList.add(c);
			}
		}
		Channel[] tmp = new Channel[channelArrayList.size()];
		return channelArrayList.toArray(tmp);
	}

	/**
	 * @param id {@link Channel} ID
	 * @return {@link Channel} object with the given ID.
	 */
	public Channel getChannel(String id)
	{
		for(Channel c : getChannels())
		{
			if(c.id.equals(id))
			{
				return c;
			}
		}
		return null;
	}

	/**
	 * @return AFK {@link Channel}.
	 */
	public Channel getAFKChannel()
	{
		if(afk_channel_id == null)
		{
			return null;
		}
		return getChannel(afk_channel_id);
	}

	/**
	 * @return AFK {@link Embed}.
	 */
	public Channel getEmbedChannel()
	{
		if(embed_channel_id == null)
		{
			return null;
		}
		return getChannel(embed_channel_id);
	}

	public Guild[] getArray(int size)
	{
		return new Guild[size];
	}

	public String toString()
	{
		return "{Guild \"" + this.name + "\" #" + this.id + "}";
	}
}
