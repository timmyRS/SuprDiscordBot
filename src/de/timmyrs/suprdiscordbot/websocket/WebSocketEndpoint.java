package de.timmyrs.suprdiscordbot.websocket;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import javax.websocket.*;
import java.net.URI;

@SuppressWarnings("unused")
@ClientEndpoint
public class WebSocketEndpoint
{
	@Nullable
	Session userSession;
	@NotNull
	private MessageHandler messageHandler;

	WebSocketEndpoint(@NotNull URI endpointURI)
	{
		try
		{
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch(Exception e)
		{
			DiscordAPI.closeWebSocket("Connection failed.");
			DiscordAPI.getWebSocket();
		}
	}

	@OnOpen
	@Nullable
	public void onOpen(Session userSession)
	{
		Main.log("Socket", "WebSocket opened.");
		this.userSession = userSession;
	}

	@OnError
	@Nullable
	public void onError(Session userSession, Throwable e)
	{
		e.printStackTrace();
	}

	@OnClose
	@Nullable
	public void onClose(Session userSession, CloseReason reason)
	{
		Main.log("Socket", "WebSocket closed: " + reason.getReasonPhrase() + " (" + reason.getCloseCode().getCode() + ")");
		this.userSession = null;
		Main.scriptManager.fireEvent("DISCONNECTED");
		DiscordAPI.closeWebSocket(null);
		DiscordAPI.getWebSocket();
	}

	@OnMessage
	@Nullable
	public void onMessage(@Nullable String msg)
	{
		if(this.messageHandler != null)
		{
			if(Main.debug)
			{
				Main.log("Socket", "> " + msg);
			}
			this.messageHandler.handleMessage(msg);
		} else if(msg != null)
		{
			Main.log("Socket", "Unhandled > " + msg);
		}
	}

	void addMessageHandler(MessageHandler msgHandler)
	{
		this.messageHandler = msgHandler;
	}

	@Nullable
	void send(String msg)
	{
		if(Main.debug)
		{
			Main.log("Socket", "< " + msg);
		}
		if(this.userSession != null && this.userSession.isOpen())
		{
			this.userSession.getAsyncRemote().sendText(msg);
		}
	}

	public interface MessageHandler
	{
		void handleMessage(@Nullable String message);
	}
}
