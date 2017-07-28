package de.timmyrs.suprdiscordbot.structures;

import com.google.gson.JsonObject;
import de.timmyrs.suprdiscordbot.Configuration;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

/**
 * User Structure.
 * You can get a user structure from, for example, {@link Member#user} and {@link Message#author}.
 *
 * @author timmyRS
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class User extends Structure
{
	/**
	 * The user's ID.
	 */
	public String id;
	/**
	 * The user's username.
	 */
	public String username;
	/**
	 * The user avatar's hash.
	 * Use {@link User#getPic()} to get a full URL.
	 */
	public String avatar;
	/**
	 * Is this user a bot?
	 */
	public boolean bot;
	/**
	 * The user's 4-digit Discord-tag.
	 */
	public String discriminator;

	/**
	 * @return {@link Configuration}
	 */
	public Configuration getValues()
	{
		return Main.getValuesConfig("u" + this.id);
	}

	/**
	 * @return Profile Picture URL.
	 */
	public String getPic()
	{
		return "https://cdn.discordapp.com/avatars/" + this.id + "/" + this.avatar + ".png";
	}

	/**
	 * @return User's DiscordTag.
	 */
	public String getTag()
	{
		return username + "#" + discriminator;
	}

	/**
	 * @return Handle of this user to be used in a message.
	 */
	public String getHandle()
	{
		return "<@" + id + ">";
	}

	/**
	 * Gets the {@link Member} object of this User in the given Guild
	 *
	 * @param g {@link Guild} object
	 * @return {@link Member} object
	 * @since 1.2
	 */
	public Member getMember(Guild g)
	{
		return g.getMember(this);
	}

	/**
	 * Gets DM Channel with this User.
	 * <p>
	 * <code>
	 * msg.getAuthor().getDMChannel().sendMessage("Hello, world!");
	 * </code>
	 *
	 * @return {@link Channel} object of DM Channel.
	 */
	public Channel getDMChannel()
	{
		for(Channel c : Main.discordAPI.getDMs())
		{
			if(c.type == 1 && c.recipients[0].id.equals(id))
			{
				return c;
			}
		}
		JsonObject json = new JsonObject();
		json.addProperty("recipient_id", id);
		return (Channel) DiscordAPI.request("POST", "/users/@me/channels", json.toString(), new Channel());
	}

	/**
	 * Sends a DM to the user.
	 * <p>
	 * <code>
	 * msg.getAuthor().sendDM("Hello, world!");
	 * </code>
	 *
	 * @param content Content of the message
	 * @return {@link Message} object of the newly sent message.
	 */
	public Message sendDM(String content)
	{
		return this.getDMChannel().sendMessage(content);
	}

	public User[] getArray(int size)
	{
		return new User[size];
	}

	public String toString()
	{
		return "{User \"" + getTag() + "\" #" + id + "}";
	}
}
