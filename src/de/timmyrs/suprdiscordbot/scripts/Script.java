package de.timmyrs.suprdiscordbot.scripts;

import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.ScriptAPI;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.function.Consumer;

public class Script
{
	public final HashMap<String, Consumer<Object>> events = new HashMap<>();
	public String name;
	String hash;
	private boolean started = false;

	Script(String name, String hash, String script) throws ScriptException
	{
		this.name = name;
		setScript(hash, script);
	}


	Script setScript(final String hash, final String script) throws ScriptException
	{
		synchronized(events)
		{
			if(started)
			{
				if(events.containsKey("UNLOAD"))
				{
					events.get("UNLOAD").accept(null);
				}
			}
			events.clear();
			final ScriptEngine engine = Main.scriptManager.factory.getEngineByExtension("js");
			engine.put("console", Main.consoleAPI);
			engine.put("discord", Main.discordAPI);
			engine.put("internet", Main.internetAPI);
			engine.put("permission", Main.permissionAPI);
			engine.put("script", new ScriptAPI(this));
			engine.eval(script);
			this.hash = hash;
			started = true;
		}
		return this;
	}


	public void fireEvent(String event, final Object data)
	{
		event = event.toUpperCase();
		synchronized(events)
		{
			if(this.events.containsKey(event))
			{
				final Consumer<Object> function = this.events.get(event);
				new Thread(()->
				{
					function.accept(data);
				}, name + " " + event).start();
			}
		}
	}
}
