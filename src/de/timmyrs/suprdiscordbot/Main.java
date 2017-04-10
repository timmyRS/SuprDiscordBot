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
	public static final int versionInt = 1100;
	public static final String version = "1.1";
	private static final File valuesDir = new File("values");
	private final static File confFile = new File("config.json");
	public static boolean debug = false;
	public static Configuration configuration;
	public static ScriptManager scriptManager;
	public static DiscordAPI discordAPI;
	public static boolean ready = false;

	public static void main(String[] args)
	{
		System.out.println("                SuprDiscordBot Version " + version);
		System.out.println("                https://github.com/timmyrs/SuprDiscordBot");
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
			new RAMCleaner();
		} else
		{
			Main.configuration.set("botToken", "BOT_TOKEN");
			System.out.println("[Setup]          Welcome to SuprDiscordBot. :) Please setup a Discord Application.");
			System.out.println("[Setup]          https://github.com/timmyrs/SuprDiscordBot/blob/master/SETUP.md");
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
