package de.timmyrs.suprdiscordbot.websocket;

import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

public class WebSocketHeart extends Thread
{
	static boolean gotACK = true;
	static int interval;
	private static long lastHeartbeat = 0;

	public WebSocketHeart()
	{
		this.start();
	}

	public void run()
	{
		//noinspection InfiniteLoopStatement
		do
		{
			if(interval != 0)
			{
				if(lastHeartbeat < System.currentTimeMillis() - interval)
				{
					if(gotACK)
					{
						gotACK = false;
						try
						{
							Main.discordAPI.send(1, WebSocket.lastSeq);
						}
						catch(DeploymentException | IOException | URISyntaxException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						interval = 0;
						DiscordAPI.closeWebSocket("Discord did not answer heartbeat.");
					}
					lastHeartbeat = System.currentTimeMillis();
				}
			}
			try
			{
				Thread.sleep(200);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		while(true);
	}
}
