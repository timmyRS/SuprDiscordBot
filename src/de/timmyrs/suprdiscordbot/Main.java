package de.timmyrs.suprdiscordbot;

import de.timmyrs.suprdiscordbot.apis.DiscordAPI;
import de.timmyrs.suprdiscordbot.scripts.ScriptManager;
import de.timmyrs.suprdiscordbot.websocket.WebSocketHeart;

import java.io.File;

/**
 * SuprDiscordBot
 *
 * @author timmyRS
 * @version 1.1
 * @see DiscordAPI
 * @see de.timmyrs.suprdiscordbot.apis.ScriptAPI
 * @see de.timmyrs.suprdiscordbot.apis.ConsoleAPI
 * @see de.timmyrs.suprdiscordbot.structures.Guild
 */
public class Main
{
	public static final int versionInt = 1200;
	public static final String version = "1.2";
	private static final File valuesDir = new File("values");
	private final static File confFile = new File("config.json");
	public static boolean debug = false;
	public static Configuration configuration;
	public static ScriptManager scriptManager;
	public static DiscordAPI discordAPI;
	public static boolean ready = false;

	public static void main(String[] args)
	{
		Main.log("Main", "SuprDiscordBot Version " + version);
		Main.log("Main", "https://github.com/timmyrs/SuprDiscordBot");
		for(String arg : args)
		{
			if(arg.equals("--debug"))
			{
				Main.debug = true;
			}
		}
		Main.scriptManager = new ScriptManager();
		Main.configuration = new Configuration(confFile);
		if(Main.configuration.has("botToken"))
		{
			Main.discordAPI = new DiscordAPI();
			new WebSocketHeart();
			DiscordAPI.getWebSocket();
		} else
		{
			Main.configuration.set("botToken", "BOT_TOKEN");
			Main.log("Setup", "Welcome to SuprDiscordBot. :) Please setup a");
			Main.log("Setup", "Discord Application using the following guide.");
			Main.log("Setup", "https://github.com/timmyrs/SuprDiscordBot/blob/master/SETUP.md");
		}
	}

	public static void log(String from, String msg)
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
