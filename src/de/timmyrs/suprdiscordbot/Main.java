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
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * SuprDiscordBot's Main Class
 * <p>
 * Arguments:
 * <ul>
 * <li>
 * <strong>--debug</strong>
 * makes output more verbose and makes {@link DiscordAPI#isInDebugMode()} return true
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
	private static final File valuesDir = new File("values");
	private static final File confFile = new File("config.json");
	private static FileWriter logFileWriter;
	public static boolean debug = false;
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
		logFileWriter = new FileWriter(new File("latest.log"), false);
		Main.log("Main", "SuprDiscordBot");
		Main.log("Main", "https://github.com/timmyrs/SuprDiscordBot");
		for(String arg : args)
		{
			switch(arg)
			{
				case "--debug":
					Main.debug = true;
					break;
				default:
					Main.log("Main", "Unknown Argument: " + arg);
					break;
			}
		}
		Main.jsonParser = new JsonParser();
		Main.configuration = new Configuration(confFile);
		if(!Main.configuration.has("botToken"))
		{
			Main.configuration.set("botToken", "BOT_TOKEN");
		}
		if(Main.configuration.getString("botToken").equals("BOT_TOKEN"))
		{
			Main.log("Setup", "Welcome to SuprDiscordBot. Please set up a Discord Application. Check the readme for details.");
		}
		else
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
		try
		{
			logFileWriter.write(from + msg + "\n");
			logFileWriter.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
