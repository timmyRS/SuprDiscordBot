package de.timmyrs.suprdiscordbot;

import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import java.util.Map.Entry;

public class RAMCleaner extends Thread
{
	public RAMCleaner()
	{
		new Thread(this, "RAMCleaner").start();
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				long milis = System.currentTimeMillis();
				int removed = 0;
				for(Entry<String, Long> entry : DiscordAPI.rate_limits.entrySet())
				{
					if(milis > entry.getValue())
					{
						DiscordAPI.rate_limits.remove(entry.getKey());
						removed++;
					}
				}
				if(removed > 0 && Main.debug)
				{
					Main.log("Cleaner", "Removed " + removed + " useless values from RAM.");
				}
				Thread.sleep(60000);
			} catch(Exception e)
			{
				try
				{
					Thread.sleep(100);
				} catch(InterruptedException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
}
