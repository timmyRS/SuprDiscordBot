package de.timmyrs.suprdiscordbot.structures;

import com.google.gson.JsonObject;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Message Structure.
 * You can retrieve an array of message structures using {@link Channel#getMessages(int)}.
 *
 * @author timmyRS
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class Message extends Structure
{
	/**
	 * ID of the message.
	 */
	public String id;
	/**
	 * Use {@link Message#getAuthor()} to get the author of this message and {@link Member#user} to get the {@link User} object.
	 */
	public User author;
	/**
	 * Contents of the message.
	 */
	public String content;
	/**
	 * When this message was sent.
	 */
	public String timestamp;
	/**
	 * When this message was edited or null.
	 */
	public String edited_timestamp;
	/**
	 * Whether this is/was a TTS message.
	 */
	public boolean tts;
	/**
	 * Whether this message mentions everyone.
	 */
	public boolean mention_everyone;
	/**
	 * Users specifically mentioned in the message.
	 */
	public User[] mentions;
	/**
	 * Roles specifically mentioned in this message.
	 */
	public String[] mention_roles;
	/**
	 * Any attached files.
	 */
	public Attachment[] attachments;
	/**
	 * Any embedded content.
	 */
	public Embed[] embeds;
	/**
	 * Reactions to this message.
	 */
	public Reaction[] reactions;
	/**
	 * Used for validating a message was sent.
	 */
	public String nonce;
	/**
	 * Is this message pinned?
	 *
	 * @see Message#isPinned()
	 */
	public boolean pinned;
	/**
	 * If the message is generated by a webhook, this is the webhook's ID.
	 */
	public String webhook_id;
	private Embed embed;
	private String channel_id;

	/**
	 * @return {@link User} object of the author.
	 * @since 1.2
	 */
	public User getAuthor()
	{
		if(this.author == null)
		{
			return ((Message) DiscordAPI.request("GET", "/channels/" + this.id + "/messages/" + id, new Message())).author;
		}
		return this.author;
	}

	/**
	 * @return {@link Channel} this message was sent in.
	 */
	public Channel getChannel()
	{
		return Main.discordAPI.getChannel(channel_id);
	}

	/**
	 * @return {@link Guild} this message was sent in.
	 * @since 1.2
	 */
	public Guild getGuild()
	{
		return this.getChannel().getGuild();
	}

	/**
	 * @return Is this message pinned?
	 * @since 1.2
	 */
	public boolean isPinned()
	{
		return this.pinned;
	}

	/**
	 * Adds a reaction to the Message.
	 * <p>
	 * <code>
	 * msg.addReaction("🔥");
	 * </code>
	 *
	 * @param emoji Emoji to be added to the message
	 * @return this
	 */
	public Message addReaction(String emoji)
	{
		try
		{
			DiscordAPI.request("PUT", "/channels/" + channel_id + "/messages/" + id + "/reactions/" + URLEncoder.encode(emoji, "UTF-8") + "/@me");
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * @param emojis List of emojis to be added to the message
	 * @return this
	 */
	public Message addReactions(final String[] emojis)
	{
		new Thread(()->
		{
			for(String emoji : emojis)
			{
				addReaction(emoji);
				try
				{
					Thread.sleep(500);
				} catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}).run();
		return this;
	}

	/**
	 * @param content New content of this message
	 * @return this
	 */
	public Message edit(String content)
	{
		JsonObject json = new JsonObject();
		json.addProperty("content", content);
		Message res = (Message) DiscordAPI.request("PATCH", "/channels/" + channel_id + "/messages/" + id, json.toString(), new Message());
		if(res != null)
		{
			this.content = res.content;
		}
		return this;
	}

	/**
	 * Deletes this message.
	 * After deletion, this object is rendered almost useless.
	 *
	 * @return this
	 */
	public Message delete()
	{
		DiscordAPI.request("DELETE", "/channels/" + channel_id + "/messages/" + id);
		this.id = null;
		return this;
	}

	/**
	 * Get Time
	 *
	 * @return The UNIX timestamp of the message's creation.
	 * @see Message#getMillis()
	 * @since 1.2
	 */
	public long getTime() throws ParseException
	{
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(this.timestamp.substring(0, this.timestamp.length() - 9)).getTime() / 1000L;
	}

	/**
	 * Get Time Millis
	 *
	 * @return The time millis of the message's creation.
	 * @see Message#getTime()
	 * @since 1.2
	 */
	public long getMillis() throws ParseException
	{
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(this.timestamp.substring(0, this.timestamp.length() - 9)).getTime();
	}

	/**
	 * Get Edit Time
	 *
	 * @return The UNIX timestamp of the message's last edit or 0 if not edited.
	 * @see Message#getEditMillis()
	 * @since 1.2
	 */
	public long getEditTime() throws ParseException
	{
		if(this.edited_timestamp == null)
		{
			return 0;
		}
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(this.edited_timestamp.substring(0, this.edited_timestamp.length() - 9)).getTime() / 1000L;
	}

	/**
	 * Get Edit Time Millis
	 *
	 * @return The time millis of the message's last edit or 0 if not edited.
	 * @see Message#getEditTime()
	 * @since 1.2
	 */
	public long getEditMillis() throws ParseException
	{
		if(this.edited_timestamp == null)
		{
			return 0;
		}
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(this.edited_timestamp.substring(0, this.edited_timestamp.length() - 9)).getTime();
	}

	public Message[] getArray(int size)
	{
		return new Message[size];
	}

	public String toString()
	{
		if(author == null)
		{
			return "{Message \"" + content + "\"}";
		} else
		{
			return "{Message #" + id +
					" by " + author.toString() +
					" in " + getChannel().toString() + "}";
		}
	}
}
