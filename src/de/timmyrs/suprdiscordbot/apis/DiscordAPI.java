package de.timmyrs.suprdiscordbot.apis;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.structures.*;
import de.timmyrs.suprdiscordbot.websocket.WebSocket;
import org.apache.commons.io.IOUtils;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Discord API ('discord')
 *
 * @author timmyRS
 */
@SuppressWarnings("unused")
public class DiscordAPI
{
	public static ArrayList<Guild> guilds = new ArrayList<>();
	public static HashMap<String, Long> rate_limits = new HashMap<>();
	private static ArrayList<Channel> dms;
	private static WebSocket ws;
	/**
	 * {@link User} object of this bot.
	 */
	public User user;

	/**
	 * Not accessible within script.
	 *
	 * @param endpoint Endpoint
	 * @return Object
	 */
	public static Object request(String endpoint)
	{
		return request("GET", endpoint, "", null);
	}

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
	 * @param method   Method
	 * @param endpoint Endpoint
	 * @return Object
	 */
	public static Object request(String method, String endpoint)
	{
		return request(method, endpoint, "", null);
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
	 * @param method   Method
	 * @param endpoint Endpoint
	 * @param args     Args
	 * @return Object
	 */
	public static Object request(String method, String endpoint, String args)
	{
		return request(method, endpoint, args, null);
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
		while(rate_limits.containsKey(endpoint) && System.currentTimeMillis() < rate_limits.get(endpoint))
		{
			try
			{
				Thread.sleep(50);
			} catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		rate_limits.put(endpoint, System.currentTimeMillis() + 3000L);
		HttpURLConnection con = null;
		if(method.equals("GET") && !args.equals(""))
		{
			endpoint += "?" + args;
		}
		try
		{
			con = (HttpURLConnection) new URL("https://discordapp.com/api" + endpoint).openConnection();
			Field delegate = HttpsURLConnectionImpl.class.getDeclaredField("delegate");
			delegate.setAccessible(true);
			Object target = delegate.get(con);
			Field field = HttpURLConnection.class.getDeclaredField("method");
			field.setAccessible(true);
			field.set(target, method);
		} catch(Exception e)
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
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			try
			{
				con.connect();
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		rate_limits.put(endpoint, System.currentTimeMillis() + 500L);
		InputStream is;
		try
		{
			is = con.getInputStream();
		} catch(Exception e)
		{
			try
			{
				is = con.getErrorStream();
				String res = IOUtils.toString(is, "UTF-8");
				if(res.startsWith("{"))
				{
					JsonObject json = new JsonParser().parse(res).getAsJsonObject();
					System.out.println(con.getResponseCode() + " - " + json.toString());
					if(json.has("message"))
					{
						if(json.get("message").getAsString().equals("You are being rate limited."))
						{
							rate_limits.put(endpoint, System.currentTimeMillis() + (json.get("retry_after").getAsLong()));
							DiscordAPI.request(method, endpoint, args, structure);
						}
					}
					return null;
				}
				System.out.println(con.getResponseCode() + " - " + res);
			} catch(Exception ex)
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
			} else if(res.startsWith("{"))
			{
				return new Gson().fromJson(res, structure.getClass());
			} else if(res.startsWith("["))
			{
				ArrayList<Structure> arrayList = new ArrayList<>();
				for(JsonElement e : new JsonParser().parse(res).getAsJsonArray())
				{
					arrayList.add(new Gson().fromJson(e, structure.getClass()));
				}
				Structure[] arr = structure.getArray(arrayList.size());
				return arrayList.toArray(arr);
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Not accessible within script.
	 *
	 * @return WebSocket
	 */
	public static WebSocket getWebSocket()
	{
		if(ws == null)
		{
			if(!Main.configuration.has("gateway"))
			{
				JsonObject json = new JsonParser().parse(DiscordAPI.request("/gateway").toString()).getAsJsonObject();
				Main.configuration.set("gateway", json.get("url").getAsString());
			}
			ws = new WebSocket(Main.configuration.getString("gateway") + "/?v=5&encoding=json");
		}
		return ws;
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
			if(reason != null)
			{
				ws.close(reason);
			}
			ws = null;
		}
	}

	/**
	 * Sends a request through the Gateway/Websocket.
	 * See https://discordapp.com/developers/docs/topics/gateway for the documentation.
	 *
	 * @param op OP Code
	 * @param d  Raw JSON Data
	 */
	public void send(int op, Object d)
	{
		JsonObject json = new JsonObject();
		json.addProperty("op", op);
		if(d instanceof JsonElement)
		{
			json.add("d", (JsonElement) d);
		} else if(d instanceof String && d.toString().startsWith("{"))
		{
			json.add("d", new JsonParser().parse(d.toString()).getAsJsonObject());
		} else
		{
			json.addProperty("d", d.toString());
		}
		if(ws == null)
		{
			WebSocket.afterConnectSend = json;
			DiscordAPI.getWebSocket();
		} else
		{
			ws.send(json);
		}
	}

	/**
	 * Gets Guild by ID
	 *
	 * @param id ID of the Guild
	 * @return Guild object with given ID
	 * @see Guild
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
		return (Guild) request("/guilds/" + id, new Guild());
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
			if(c.recipient.id.equals(id))
			{
				return c.recipient;
			}
		}
		return (User) request("/users/" + id, new User());
	}

	/**
	 * Gets Channel by ID
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
		if(c.is_private)
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
	 * @return An array of DM {@link Channel}s this bot is part of.
	 */
	public Channel[] getDMs()
	{
		if(dms == null)
		{
			dms = new ArrayList<>();
			for(Channel c : (Channel[]) request("/users/@me/channels", new Channel()))
			{
				dms.add(c);
			}
		}
		Channel[] ret = new Channel[dms.size()];
		return dms.toArray(ret);
	}

	/**
	 * @return New {@link Message} object
	 */
	public Message createMessage()
	{
		return new Message();
	}

	/**
	 * @return New {@link Embed} object
	 */
	public Embed createEmbed()
	{
		return new Embed();
	}
}
