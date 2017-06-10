package de.timmyrs.suprdiscordbot.scripts;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.ScriptAPI;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.function.Consumer;

public class Script
{
	public HashMap<String, Consumer<Object>> events = new HashMap<>();
	public String name;
	String hash;
	private boolean started = false;

	Script(@NotNull String name, @NotNull String hash, @NotNull String script) throws ScriptException
	{
		this.name = name;
		this.hash = hash;
		setScript(script);
	}

	@NotNull
	Script setScript(@NotNull final String script) throws ScriptException
	{
		if(this.started)
		{
			if(this.events.containsKey("UNLOAD"))
			{
				this.events.get("UNLOAD").accept(null);
			}
		}
		events.clear();
		final ScriptEngine engine = Main.scriptManager.factory.getEngineByExtension("js");
		engine.put("console", Main.consoleAPI);
		engine.put("discord", Main.discordAPI);
		engine.put("internet", Main.internetAPI);
		engine.put("permission", Main.permisisonAPI);
		engine.put("script", new ScriptAPI(this));
		engine.eval(script);
		this.started = true;
		return this;
	}

	@NotNull
	public void fireEvent(@NotNull String event, @Nullable final Object data)
	{
		event = event.toUpperCase();
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
