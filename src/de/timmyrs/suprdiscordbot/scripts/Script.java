package de.timmyrs.suprdiscordbot.scripts;

import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.ConsoleAPI;
import de.timmyrs.suprdiscordbot.apis.PermissionAPI;
import de.timmyrs.suprdiscordbot.apis.ScriptAPI;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.function.Consumer;

public class Script
{
	public HashMap<String, Consumer<Object>> events = new HashMap<>();
	public String name;
	String hash;

	Script(String name, String hash, String script) throws ScriptException
	{
		this.name = name;
		this.hash = hash;
		setScript(script);
	}

	Script setScript(final String script) throws ScriptException
	{
		events.clear();
		final ScriptEngine engine = Main.scriptManager.factory.getEngineByExtension("js");
		engine.put("script", new ScriptAPI(this));
		engine.put("discord", Main.discordAPI);
		engine.put("console", new ConsoleAPI());
		engine.put("permission", new PermissionAPI());
		Bindings bindings = engine.getBindings(100);
		bindings.remove("for");
		engine.eval(script);
		return this;
	}

	public Script fireEvent(final String event)
	{
		return fireEvent(event, null);
	}

	public Script fireEvent(String event, final Object data)
	{
		event = event.toUpperCase();
		if(events.containsKey(event))
		{
			final Consumer<Object> function = events.get(event);
			new Thread(()->
			{
				function.accept(data);
			}, name + " " + event).start();
		}
		return this;
	}
}
