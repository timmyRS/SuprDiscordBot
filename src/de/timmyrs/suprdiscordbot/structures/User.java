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
public class User extends Structure
{
	/**
	 * The user's ID
	 */
	public String id;
	/**
	 * The user's username
	 */
	public String username;
	/**
	 * The user avatar's hash
	 */
	public String avatar;
	/**
	 * Is this user a bot?
	 */
	public boolean bot;
	/**
	 * The user's 4-digit Discord-tag
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
	 * @return Profile Picture URL
	 */
	public String getPic()
	{
		return "https://cdn.discordapp.com/avatars/" + this.id + "/" + this.avatar + ".png";
	}

	/**
	 * @return User's Discord-tag.
	 */
	public String getTag()
	{
		return username + "#" + discriminator;
	}

	/**
	 * @return Handle of this user to be used in a message
	 */
	public String getHandle()
	{
		return "<@" + id + ">";
	}

	/**
	 * @return DM {@link Channel} with this user
	 */
	public Channel getDMChannel()
	{
		for(Channel c : Main.discordAPI.getDMs())
		{
			if(c.recipient.id.equals(id))
			{
				return c;
			}
		}
		JsonObject json = new JsonObject();
		json.addProperty("recipient_id", id);
		return (Channel) DiscordAPI.request("POST", "/users/@me/channels", json.toString(), new Channel());
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
