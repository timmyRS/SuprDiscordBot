package de.timmyrs.suprdiscordbot;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.sun.istack.internal.NotNull;
import de.timmyrs.suprdiscordbot.apis.ConsoleAPI;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;
import de.timmyrs.suprdiscordbot.apis.InternetAPI;
import de.timmyrs.suprdiscordbot.apis.PermissionAPI;
import de.timmyrs.suprdiscordbot.scripts.ScriptManager;
import de.timmyrs.suprdiscordbot.websocket.WebSocketEndpoint;
import de.timmyrs.suprdiscordbot.websocket.WebSocketHeart;

import java.io.File;
import java.io.IOException;
// Note: Not closing the p-tag is what JavaDoc wants. It is not a mistake.

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
 * @version 1.2
 * @see DiscordAPI
 * @see de.timmyrs.suprdiscordbot.apis.ScriptAPI
 * @see de.timmyrs.suprdiscordbot.apis.ConsoleAPI
 * @see de.timmyrs.suprdiscordbot.structures.Guild
 */
public class Main
{
	public static final int versionInt = 1200;
	public static final Object WS_LOCK = "";
	private static final String version = "1.2";
	private static final File valuesDir = new File("values");
	private final static File confFile = new File("config.json");
	public static boolean debug = false;
	public static boolean dontUpdateScripts = false;
	public static Configuration configuration;
	public static ScriptManager scriptManager;
	public static ConsoleAPI consoleAPI;
	public static DiscordAPI discordAPI;
	public static InternetAPI internetAPI;
	public static PermissionAPI permisisonAPI;
	public static boolean ready = false;
	public static Gson gson;
	public static JsonParser jsonParser;
	public static WebSocketEndpoint webSocketEndpoint;

	public static void main(String[] args)
	{
		Main.log("Main", "SuprDiscordBot Version " + version);
		Main.log("Main", "https://github.com/timmyrs/SuprDiscordBot");
		for(String arg : args)
		{
			if(arg.equals("--debug"))
			{
				Main.debug = true;
			} else if(arg.equals("--dont-update-scripts"))
			{
				Main.dontUpdateScripts = true;
			} else
			{
				Main.log("Main", "Unknown Argument: " + arg);
			}
		}
		Main.jsonParser = new JsonParser();
		Main.configuration = new Configuration(confFile);
		if(Main.configuration.has("botToken"))
		{
			Main.gson = new Gson();
			Main.scriptManager = new ScriptManager();
			Main.consoleAPI = new ConsoleAPI();
			Main.discordAPI = new DiscordAPI();
			Main.internetAPI = new InternetAPI();
			Main.permisisonAPI = new PermissionAPI();
			new WebSocketHeart();
			DiscordAPI.getWebSocket();
			try
			{
				if(Integer.valueOf(internetAPI.httpString("https://raw.githubusercontent.com/timmyrs/SuprDiscordBot/master/version.txt").trim()) > Main.versionInt)
				{
					Main.log("Main", "There is a new verion/release available at https://github.com/timmyrs/SuprDiscordBot/releases");
				} else if(Main.debug)
				{
					Main.log("Main", "This seems to be the newest version.");
				}
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			Main.configuration.set("botToken", "BOT_TOKEN");
			Main.log("Setup", "Welcome to SuprDiscordBot. :) Please setup a");
			Main.log("Setup", "Discord Application using the following guide.");
			Main.log("Setup", "https://github.com/timmyrs/SuprDiscordBot/blob/master/SETUP.md");
		}
	}

	public static void log(@NotNull String from, String msg)
	{
		from = "[" + from + "] ";
		final int length = (12 - from.length());
		for(int i = 0; i < length; i++)
		{
			from += " ";
		}
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
