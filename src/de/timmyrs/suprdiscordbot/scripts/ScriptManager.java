package de.timmyrs.suprdiscordbot.scripts;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ScriptManager
{
	ScriptEngineManager factory;
	private ArrayList<Script> scripts = new ArrayList<>();
	private ArrayList<FailedScript> failedscripts = new ArrayList<>();

	public ScriptManager()
	{
		this.factory = new ScriptEngineManager();
	}

	@Nullable
	Script getScript(@NotNull final String name)
	{
		for(Script script : scripts)
		{
			if(script.name.equals(name))
			{
				return script;
			}
		}
		return null;
	}

	@Nullable
	FailedScript getFailedScript(@NotNull final String name)
	{
		for(FailedScript script : failedscripts)
		{
			if(script.name.equals(name))
			{
				return script;
			}
		}
		return null;
	}

	@Nullable
	void removeScript(@NotNull final Script script)
	{
		if(scripts.contains(script))
		{
			scripts.remove(script);
		}
	}

	@Nullable
	void removeFailedScript(@NotNull final FailedScript script)
	{
		if(failedscripts.contains(script))
		{
			failedscripts.remove(script);
		}
	}

	@NotNull
	Script registerScript(@NotNull final File f) throws ScriptException
	{
		String cont = "";
		try
		{
			cont = IOUtils.toString(new BufferedInputStream(new FileInputStream(f)), "UTF-8");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		Script s = new Script(f.getName(), DigestUtils.sha384Hex(cont), cont);
		this.scripts.add(s);
		return s;
	}

	@NotNull
	void registerFailedScript(@NotNull final File f)
	{
		String cont = "";
		try
		{
			cont = IOUtils.toString(new BufferedInputStream(new FileInputStream(f)), "UTF-8");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		FailedScript s = new FailedScript(f.getName(), DigestUtils.sha384Hex(cont));
		this.failedscripts.add(s);
	}

	@NotNull
	public void fireEvent(@NotNull final String event)
	{
		fireEvent(event, null);
	}

	@NotNull
	public void fireEvent(@NotNull final String event, @Nullable final Object data)
	{
		for(Script script : scripts)
		{
			script.fireEvent(event, data);
		}
	}
}
