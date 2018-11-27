package de.timmyrs.suprdiscordbot.apis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.structures.Channel;
import de.timmyrs.suprdiscordbot.structures.Embed;
import de.timmyrs.suprdiscordbot.structures.Guild;
import de.timmyrs.suprdiscordbot.structures.Overwrite;
import de.timmyrs.suprdiscordbot.structures.Presence;
import de.timmyrs.suprdiscordbot.structures.Structure;
import de.timmyrs.suprdiscordbot.structures.User;
import de.timmyrs.suprdiscordbot.websocket.WebSocket;
import org.apache.commons.io.IOUtils;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Discord API ('discord')
 *
 * @author timmyRS
 */
@SuppressWarnings({"unused", "SameParameterValue", "UnusedReturnValue", "WeakerAccess"})
public class DiscordAPI
{
	public static final HashMap<String, Long> rate_limits = new HashMap<>();
	public static ArrayList<Guild> guilds = new ArrayList<>();
	private static ArrayList<Channel> dms;
	private static WebSocket ws;
	/**
	 * {@link User} object of this bot.
	 */
	public User user;

	/**
	 * Not accessible within script.
	 *
	 * @param endpoint  Endpoint
	 * @param structure Structure
	 * @return Object
	 */
	public static Object request(String endpoint, Structure structure)
	{
		return request("GET", endpoint, "", structure);
	}

	/**
	 * Not accessible within script.
	 *
	 * @param method    Method
	 * @param endpoint  Endpoint
	 * @param structure Structure
	 * @return Object
	 */
	public static Object request(String method, String endpoint, Structure structure)
	{
		return request(method, endpoint, "", structure);
	}

	/**
	 * Not accessible within script.
	 *
	 * @param method    Method
	 * @param endpoint  Endpoint
	 * @param args      Args
	 * @param structure Structure
	 * @return Object
	 */
	public static Object request(String method, String endpoint, String args, Structure structure)
	{
		long rate_limit = 0;
		synchronized(rate_limits)
		{
			if(rate_limits.containsKey(endpoint))
			{
				rate_limit = rate_limits.get(endpoint);
			}
		}
		if(rate_limit != 0)
		{
			while(System.currentTimeMillis() < rate_limit)
			{
				try
				{
					Thread.sleep(50);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			synchronized(rate_limits)
			{
				rate_limits.put(endpoint, System.currentTimeMillis() + 3000L);
			}
		}
		HttpURLConnection con = null;
		if(method.equals("GET") && !args.equals(""))
		{
			endpoint += "?" + args;
		}
		try
		{
			con = (HttpURLConnection) new URL("https://discordapp.com/api/v6/" + endpoint).openConnection();
			Field delegate = HttpsURLConnectionImpl.class.getDeclaredField("delegate");
			delegate.setAccessible(true);
			Object target = delegate.get(con);
			Field field = HttpURLConnection.class.getDeclaredField("method");
			field.setAccessible(true);
			field.set(target, method);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(con == null)
		{
			return null;
		}
		con.setRequestProperty("Authorization", "Bot " + Main.configuration.getString("botToken"));
		con.setRequestProperty("User-Agent", "DiscordBot (https://discord.gg, 1.0)");
		if(!method.equals("GET") && args != null)
		{
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
			try
			{
				con.getOutputStream().write(args.getBytes());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				con.connect();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		synchronized(rate_limits)
		{
			rate_limits.put(endpoint, System.currentTimeMillis() + 500L);
		}
		InputStream is;
		try
		{
			is = con.getInputStream();
		}
		catch(Exception e)
		{
			try
			{
				is = con.getErrorStream();
				String res = IOUtils.toString(is, "UTF-8");
				if(res.startsWith("{"))
				{
					JsonObject json = Main.jsonParser.parse(res).getAsJsonObject();
					if(Main.debug)
					{
						throw new RuntimeException(con.getResponseCode() + " - " + json.toString());
					}
					else
					{
						Main.log("Discord", con.getResponseCode() + " " + json.toString() + " in response to " + method + " " + endpoint);
					}
					if(json.has("message"))
					{
						if(json.get("message").getAsString().equals("You are being rate limited."))
						{
							synchronized(rate_limits)
							{
								rate_limits.put(endpoint, System.currentTimeMillis() + (json.get("retry_after").getAsLong()));
							}
							DiscordAPI.request(method, endpoint, args, structure);
						}
					}
					return null;
				}
				if(Main.debug)
				{
					throw new RuntimeException(con.getResponseCode() + " - " + res);
				}
				else
				{
					Main.log("Discord", con.getResponseCode() + " " + res + " in response to " + method + " " + endpoint);
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return null;
		}
		try
		{
			String res = IOUtils.toString(is, "UTF-8");
			if(structure == null)
			{
				return res;
			}
			else if(res.startsWith("{"))
			{
				return Main.gson.fromJson(res, structure.getClass());
			}
			else if(res.startsWith("["))
			{
				ArrayList<Structure> arrayList = new ArrayList<>();
				for(JsonElement e : Main.jsonParser.parse(res).getAsJsonArray())
				{
					arrayList.add(Main.gson.fromJson(e, structure.getClass()));
				}
				Structure[] arr = structure.getArray(arrayList.size());
				return arrayList.toArray(arr);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Not accessible within script.
	 */
	public static void openWebSocket() throws DeploymentException, IOException, URISyntaxException
	{
		if(ws == null)
		{
			if(!Main.configuration.has("gateway"))
			{
				Main.configuration.set("gateway", Main.jsonParser.parse(Main.discordAPI.request("/gateway").toString()).getAsJsonObject().get("url").getAsString());
			}
			ws = new WebSocket(Main.configuration.getString("gateway") + "/?v=6&encoding=json");
		}
	}

	/**
	 * Not accessible within script.
	 *
	 * @param reason Close reason
	 */
	public static void closeWebSocket(String reason)
	{
		if(ws != null)
		{
			ws.close(reason);
			ws = null;
		}
	}

	/**
	 * Send a manual request to the Discord API.
	 *
	 * @param method   HTTP Method (GET, POST, PATCH, etc.)
	 * @param endpoint The API endpoint (e.g. /users/_ID_)
	 * @return Object
	 */
	public static Object request(String method, String endpoint)
	{
		return request(method, endpoint, "", null);
	}

	/**
	 * Send a manual request to the Discord API.
	 *
	 * @param method   HTTP Method (GET, POST, PATCH, etc.)
	 * @param endpoint The API endpoint (e.g. /users/_ID_)
	 * @param args     HTTP Arguments
	 * @return Object
	 */
	public static Object request(String method, String endpoint, String args)
	{
		return request(method, endpoint, args, null);
	}

	/**
	 * Send a manual request to the Discord API.
	 *
	 * @param endpoint HTTP Method (GET, POST, PATCH, etc.)
	 * @return Object
	 */
	public Object request(String endpoint)
	{
		return request("GET", endpoint, "", null);
	}

	/**
	 * Sends a request through the Gateway/Websocket.
	 * See https://discordapp.com/developers/docs/topics/gateway for the documentation.
	 *
	 * @param op OP Code
	 * @param d  Raw JSON Data
	 */
	public void send(int op, Object d) throws DeploymentException, IOException, URISyntaxException
	{
		JsonObject json = new JsonObject();
		json.addProperty("op", op);
		if(d instanceof JsonElement)
		{
			json.add("d", (JsonElement) d);
		}
		else if(d instanceof String && d.toString().startsWith("{"))
		{
			json.add("d", Main.jsonParser.parse(d.toString()).getAsJsonObject());
		}
		else
		{
			json.addProperty("d", d.toString());
		}
		if(ws == null)
		{
			WebSocket.afterConnectSend = json;
			DiscordAPI.openWebSocket();
		}
		else
		{
			ws.send(json);
		}
	}

	/**
	 * Gets {@link Guild} object by ID
	 *
	 * @param id ID of the Guild
	 * @return {@link Guild} object with given ID
	 */
	public Guild getGuild(String id)
	{
		for(Guild g : guilds)
		{
			if(g.id.equals(id))
			{
				return g;
			}
		}
		Guild g = (Guild) request("/guilds/" + id, new Guild());
		if(g != null)
		{
			guilds.add(g);
		}
		return g;
	}

	/**
	 * Gets {@link Guild} object by Name
	 *
	 * @param name Name of the Guild
	 * @return {@link Guild} object with given name or null if not found
	 * @since 1.2
	 */
	public Guild getGuildByName(String name)
	{
		for(Guild g : guilds)
		{
			if(g.name.equalsIgnoreCase(name))
			{
				return g;
			}
		}
		return null;
	}

	/**
	 * Gets User by ID
	 *
	 * @param id ID of the User
	 * @return User object with the given ID
	 * @see User
	 */
	public User getUser(String id)
	{
		for(Channel c : getDMs())
		{
			if(c.type == 1)
			{
				if(c.recipients[0].id.equals(id))
				{
					return c.recipients[0];
				}
			}
		}
		return (User) request("/users/" + id, new User());
	}

	/**
	 * Gets {@link Presence} object by ID
	 *
	 * @param id ID of the user
	 * @return {@link Presence} object with the given ID or null if not found
	 */
	public Presence getPresence(String id)
	{
		for(Guild g : guilds)
		{
			for(Presence p : g.presences)
			{
				if(p.user.id.equals(id))
				{
					p.guild_id = null;
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Gets {@link Channel} object by ID
	 *
	 * @param id ID of the Channel
	 * @return {@link Channel} object with the given ID
	 */
	public Channel getChannel(String id)
	{
		for(Guild g : guilds)
		{
			for(Channel c : g.getChannels())
			{
				if(c.id.equals(id))
				{
					return c;
				}
			}
		}
		for(Channel c : getDMs())
		{
			if(c.id.equals(id))
			{
				return c;
			}
		}
		Channel c = (Channel) request("/channels/" + id, new Channel());
		if(c.type == 1 || c.type == 3)
		{
			dms.add(c);
		}
		return c;
	}

	/**
	 * @return An array of {@link Guild}s this bot is a member of.
	 */
	public Guild[] getGuilds()
	{
		Guild[] ret = new Guild[guilds.size()];
		return guilds.toArray(ret);
	}

	/**
	 * Returns an array of DM {@link Channel}s this bot is part of.
	 * This includes DM Channels (type 1) as well as Group DM Channels (type 3), so make sure to check the {@link Channel#type}.
	 *
	 * @return An array of DM {@link Channel}s this bot is part of.
	 */
	public Channel[] getDMs()
	{
		if(dms == null)
		{
			dms = new ArrayList<>();
			dms.addAll(Arrays.asList((Channel[]) request("/users/@me/channels", new Channel())));
		}
		Channel[] ret = new Channel[dms.size()];
		return dms.toArray(ret);
	}

	/**
	 * @return New {@link Embed} object
	 */
	public Embed createEmbed()
	{
		return new Embed();
	}

	/**
	 * @return New {@link Overwrite} object
	 * @since 1.1
	 */
	public Overwrite createOverwrite()
	{
		return new Overwrite();
	}

	/**
	 * Tells you weather this instance is running in debug mode.
	 *
	 * @return Weather this instance is running in debug mode.
	 * @since 1.2
	 */
	public boolean isInDebugMode()
	{
		return Main.debug;
	}
}
