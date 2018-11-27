package de.timmyrs.suprdiscordbot.websocket;

import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("unused")
@ClientEndpoint
public class WebSocketEndpoint
{
	Session userSession;
	private MessageHandler messageHandler;

	WebSocketEndpoint(URI endpointURI) throws IOException, DeploymentException
	{
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(this, endpointURI);
	}

	@OnOpen
	public void onOpen(Session userSession)
	{
		Main.log("Socket", "WebSocket opened.");
		this.userSession = userSession;
	}

	@OnError
	public void onError(Session userSession, Throwable e)
	{
		e.printStackTrace();
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) throws DeploymentException, IOException, URISyntaxException
	{
		Main.log("Socket", "WebSocket closed: " + reason.getReasonPhrase() + " (" + reason.getCloseCode().getCode() + ")");
		this.userSession = null;
		Main.scriptManager.fireEvent("DISCONNECTED");
		DiscordAPI.closeWebSocket(null);
		DiscordAPI.openWebSocket();
	}

	@OnMessage
	public void onMessage(String msg) throws DeploymentException, IOException, URISyntaxException
	{
		if(this.messageHandler != null)
		{
			if(Main.debug)
			{
				Main.log("Socket", "> " + msg);
			}
			this.messageHandler.handleMessage(msg);
		}
		else if(msg != null)
		{
			Main.log("Socket", "Unhandled: " + msg);
		}
	}

	void addMessageHandler(MessageHandler msgHandler)
	{
		this.messageHandler = msgHandler;
	}

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
		void handleMessage(String message) throws DeploymentException, IOException, URISyntaxException;
	}
}
