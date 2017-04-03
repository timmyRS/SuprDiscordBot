package de.timmyrs.suprdiscordbot.websocket;

import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import javax.websocket.*;
import java.net.URI;

@SuppressWarnings("unused")
@ClientEndpoint
public class WebSocketEndpoint
{
	Session userSession = null;
	private MessageHandler messageHandler;

	public WebSocketEndpoint(URI endpointURI)
	{
		try
		{
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@OnOpen
	public void onOpen(Session userSession)
	{
		System.out.println("[WebSocket]     WebSocket opened.");
		this.userSession = userSession;
	}

	@OnError
	public void onError(Session userSession, Throwable e)
	{
		e.printStackTrace();
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason)
	{
		System.out.println("[WebSocket]     WebSocket closed: " + reason.getReasonPhrase() + " (" + reason.getCloseCode().getCode() + ")");
		this.userSession = null;
		Main.scriptManager.fireEvent("DISCONNECTED");
		DiscordAPI.closeWebSocket(null);
		DiscordAPI.getWebSocket();
	}

	@OnMessage
	public void onMessage(String msg)
	{
		System.out.println("[WebSocket]     > " + msg);
		this.messageHandler.handleMessage(msg);
	}

	void addMessageHandler(MessageHandler msgHandler)
	{
		this.messageHandler = msgHandler;
	}

	void send(String msg)
	{
		System.out.println("[WebSocket]     < " + msg);
		this.userSession.getAsyncRemote().sendText(msg);
	}

	public interface MessageHandler
	{
		void handleMessage(String message);
	}
}
