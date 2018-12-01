package de.timmyrs.suprdiscordbot.websocket;

import de.timmyrs.suprdiscordbot.Main;

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
	final private MessageHandler messageHandler;
	public Session session;

	WebSocketEndpoint(URI endpointURI, MessageHandler messageHandler) throws IOException, DeploymentException
	{
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(this, endpointURI);
		this.messageHandler = messageHandler;
	}

	@OnOpen
	public void onOpen(Session session)
	{
		Main.log("Socket", "WebSocket opened.");
		this.session = session;
	}

	@OnError
	public void onError(Session userSession, Throwable e)
	{
		e.printStackTrace();
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason)
	{
		Main.log("Socket", "WebSocket closed: " + reason.getReasonPhrase() + " (" + reason.getCloseCode().getCode() + ")");
		System.exit(0);
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

	public interface MessageHandler
	{
		void handleMessage(String message) throws DeploymentException, IOException, URISyntaxException;
	}
}
