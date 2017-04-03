package de.timmyrs.suprdiscordbot.scripts;

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

	Script getScript(final String name)
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

	FailedScript getFailedScript(final String name)
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

	void removeScript(final Script script)
	{
		if(scripts.contains(script))
		{
			scripts.remove(script);
		}
	}

	void removeFailedScript(final FailedScript script)
	{
		if(failedscripts.contains(script))
		{
			failedscripts.remove(script);
		}
	}

	Script registerScript(final File f) throws ScriptException
	{
		String cont = "";
		try
		{
			cont = IOUtils.toString(new BufferedInputStream(new FileInputStream(f)), "UTF-8");
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		Script s = new Script(f.getName(), DigestUtils.sha384Hex(cont), cont);
		this.scripts.add(s);
		return s;
	}

	FailedScript registerFailedScript(final File f)
	{
		String cont = "";
		try
		{
			cont = IOUtils.toString(new BufferedInputStream(new FileInputStream(f)), "UTF-8");
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		FailedScript s = new FailedScript(f.getName(), DigestUtils.sha384Hex(cont));
		this.failedscripts.add(s);
		return s;
	}

	public ScriptManager fireEvent(final String event)
	{
		return fireEvent(event, null);
	}

	public ScriptManager fireEvent(final String event, final Object data)
	{
		for(Script script : scripts)
		{
			script.fireEvent(event, data);
		}
		return this;
	}
}
