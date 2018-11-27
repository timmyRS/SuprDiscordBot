package de.timmyrs.suprdiscordbot.scripts;

import de.timmyrs.suprdiscordbot.Main;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class ScriptWatcher extends Thread
{
	private final File scriptsDir = new File("scripts");

	public ScriptWatcher()
	{
		this.start();
	}

	public void run()
	{
		try
		{
			// Delay initial script loading, to collect data from Discord without any problems.
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		try
		{
			if(!scriptsDir.exists())
			{
				scriptsDir.mkdir();
			}
			Main.log("Watcher", "Scripts are being loaded from " + scriptsDir.getAbsolutePath());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		do
		{
			try
			{
				//noinspection ConstantConditions
				for(File f : scriptsDir.listFiles())
				{
					if(f.isDirectory())
					{
						continue;
					}
					if(!f.getName().substring(f.getName().length() - 3).equals(".js"))
					{
						continue;
					}
					Script s = Main.scriptManager.getScript(f.getName());
					FailedScript fs = null;
					if(s == null)
					{
						fs = Main.scriptManager.getFailedScript(f.getName());
					}
					try
					{
						if(s == null && fs == null)
						{
							Main.log("Watcher", "Loading " + f.getName());
							s = Main.scriptManager.registerScript(f);
						}
						else
						{
							String cont = "";
							try
							{
								cont = IOUtils.toString(new BufferedInputStream(new FileInputStream(f)), "UTF-8");
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							String hash = DigestUtils.sha384Hex(cont);
							if(s != null)
							{
								if(s.hash.equals(hash))
								{
									continue;
								}
								Main.log("Watcher", "Reloading " + s.name);
								s.hash = hash;
								s.setScript(cont);
							}
							else
							{
								if(fs.hash.equals(hash))
								{
									continue;
								}
								Main.log("Watcher", "Reloading " + fs.name);
								Main.scriptManager.removeFailedScript(fs);
								s = Main.scriptManager.registerScript(f);
							}
						}
					}
					catch(Exception e)
					{
						if(s != null)
						{
							Main.scriptManager.removeScript(s);
						}
						if(e.getMessage() == null)
						{
							e.printStackTrace();
						}
						else
						{
							Main.log("Watcher", e.getMessage() + " in " + f.getName());
						}
						if(fs == null)
						{
							Main.scriptManager.registerFailedScript(f);
						}
						continue;
					}
					if(s != null)
					{
						s.fireEvent("LOAD", null);
					}
				}
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		while(Main.liveUpdateScripts);
	}
}
