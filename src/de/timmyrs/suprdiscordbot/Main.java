package de.timmyrs.suprdiscordbot;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import de.timmyrs.suprdiscordbot.apis.ConsoleAPI;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;
import de.timmyrs.suprdiscordbot.apis.InternetAPI;
import de.timmyrs.suprdiscordbot.apis.PermissionAPI;
import de.timmyrs.suprdiscordbot.apis.ScriptAPI;
import de.timmyrs.suprdiscordbot.scripts.ScriptManager;
import de.timmyrs.suprdiscordbot.websocket.WebSocket;
import de.timmyrs.suprdiscordbot.websocket.WebSocketEndpoint;
import de.timmyrs.suprdiscordbot.websocket.WebSocketHeart;

import javax.websocket.DeploymentException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * SuprDiscordBot Main Class.
 * <p>
 * Arguments:
 * <ul>
 * <li>
 * <strong>--debug</strong>
 * makes output more verbose and makes {@link DiscordAPI#isInDebugMode()} return true
 * </li>
 * <li>
 * <strong>--dont-update-scripts</strong>
 * stops the Script Watcher from checking for new or updated scripts.
 * This is recommended for a <i>production environment</i>, as the watcher reads on your hard drive quite often.
 * </li>
 * </ul>
 *
 * @author timmyRS
 * @see DiscordAPI
 * @see ScriptAPI
 * @see ConsoleAPI
 * @see de.timmyrs.suprdiscordbot.structures.Guild
 */
public class Main
{
	private static final String version = "1.2.3";
	private static final File valuesDir = new File("values");
	private final static File confFile = new File("config.json");
	public static boolean debug = false;
	public static boolean liveUpdateScripts = false;
	public static Configuration configuration;
	public static ScriptManager scriptManager;
	public static ConsoleAPI consoleAPI;
	public static DiscordAPI discordAPI;
	public static InternetAPI internetAPI;
	public static PermissionAPI permissionAPI;
	public static boolean ready = false;
	public static Gson gson;
	public static JsonParser jsonParser;
	public static volatile WebSocketEndpoint webSocketEndpoint;

	public static void main(String[] args) throws DeploymentException, IOException, URISyntaxException
	{
		Main.log("Main", "SuprDiscordBot v" + version);
		Main.log("Main", "https://github.com/timmyrs/SuprDiscordBot");
		for(String arg : args)
		{
			switch(arg)
			{
				case "--debug":
					Main.debug = true;
					break;
				case "--live-update-scripts":
					Main.liveUpdateScripts = true;
					break;
				default:
					Main.log("Main", "Unknown Argument: " + arg);
					break;
			}
		}
		Main.jsonParser = new JsonParser();
		Main.configuration = new Configuration(confFile);
		if(Main.configuration.has("botToken") && !Main.configuration.getString("botToken").equals("BOT_TOKEN"))
		{
			Main.gson = new Gson();
			if(!Main.configuration.has("gateway"))
			{
				Main.configuration.set("gateway", Main.jsonParser.parse(Main.discordAPI.request("/gateway").toString()).getAsJsonObject().get("url").getAsString());
			}
			Main.scriptManager = new ScriptManager();
			Main.discordAPI = new DiscordAPI(new WebSocket(Main.configuration.getString("gateway") + "/?v=6&encoding=json"));
			Main.consoleAPI = new ConsoleAPI();
			Main.internetAPI = new InternetAPI();
			Main.permissionAPI = new PermissionAPI();
			new WebSocketHeart();
			try
			{
				if(!internetAPI.httpString("https://raw.githubusercontent.com/timmyrs/SuprDiscordBot/master/version.txt").trim().equals(Main.version))
				{
					Main.log("Main", "There is a new verion/release available at https://github.com/timmyrs/SuprDiscordBot/releases");
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Main.configuration.set("botToken", "BOT_TOKEN");
			Main.log("Setup", "Welcome to SuprDiscordBot. Please set up a Discord Application. Check the readme for details.");
		}
	}

	public static void log(String from, String msg)
	{
		from = "[" + from + "] ";
		final int length = (12 - from.length());
		StringBuilder fromBuilder = new StringBuilder(from);
		for(int i = 0; i < length; i++)
		{
			fromBuilder.append(" ");
		}
		from = fromBuilder.toString();
		System.out.println(from + msg);
	}

	public static Configuration getValuesConfig(String name)
	{
		if(!valuesDir.exists())
		{
			valuesDir.mkdir();
		}
		return new Configuration(new File(valuesDir.getAbsolutePath() + "/" + name + ".json"));
	}
}
