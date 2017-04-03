package de.timmyrs.suprdiscordbot.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;
import de.timmyrs.suprdiscordbot.structures.*;

import java.io.IOException;
import java.net.URI;

public class WebSocket
{
	public static String session_id = "";
	public static JsonObject afterConnectSend;
	static int lastSeq;
	private WebSocketEndpoint endpoint;

	public WebSocket(String url)
	{
		try
		{
			endpoint = new WebSocketEndpoint(new URI(url));
			endpoint.addMessageHandler(message->
			{
				JsonObject json = new JsonParser().parse(message).getAsJsonObject();
				GatewayPayload payload = new Gson().fromJson(json, GatewayPayload.class);
				switch(payload.op)
				{
					default:
						System.out.println("[WebSocket]     Unhandled Operation Code: " + payload.op + " - " + json);
						break;
					case 0:
						lastSeq = payload.s;
						User u;
						Guild g;
						Member m;
						Presence p;
						Message msg;
						JsonObject data = json.get("d").getAsJsonObject();
						switch(payload.t)
						{
							default:
								System.out.println("[WebSocket]     Unhandled Dispatch Event: " + payload.t + " - " + json);
								break;
							case "READY":
								JsonObject d = json.get("d").getAsJsonObject();
								session_id = d.get("session_id").getAsString();
								Main.ready = true;
								Main.discordAPI.user = new Gson().fromJson(d.get("user"), User.class);
							case "RESUMED":
								if(afterConnectSend != null)
								{
									send(afterConnectSend);
									afterConnectSend = null;
								}
								Main.scriptManager.fireEvent("CONNECTED");
								break;
							case "GUILD_CREATE":
								g = new Gson().fromJson(data, Guild.class);
								for(Channel c : g.getChannels())
								{
									c.guild_id = g.id;
								}
								for(Presence presence : g.presences)
								{
									presence.guild_id = g.id;
								}
								for(Member member : g.members)
								{
									member.guild_id = g.id;
								}
								for(VoiceState vs : g.voice_states)
								{
									vs.guild_id = g.id;
								}
								DiscordAPI.guilds.add(g);
								Main.scriptManager.fireEvent("GUILD_CREATE", g);
								break;
							case "GUILD_DELETE":
								g = Main.discordAPI.getGuild(data.get("id").getAsString());
								DiscordAPI.guilds.remove(g);
								Main.scriptManager.fireEvent("GUILD_DELETE", g);
								break;
							case "GUILD_MEMBER_REMOVE":
								p = new Gson().fromJson(data, Presence.class);
								p.getGuild().removePresence(p);
								Main.scriptManager.fireEvent("USER_LEAVE", p);
								break;
							case "PRESENCE_UPDATE":
								g = Main.discordAPI.getGuild(data.get("guild_id").getAsString());
								p = new Gson().fromJson(data, Presence.class);
								m = g.getMember(p);
								if(m == null)
								{
									if(!p.status.equals("offline"))
									{
										Main.scriptManager.fireEvent("USER_JOIN", p);
										g.addPresence(p);
									}
								} else
								{
									if(p.user.username != null)
									{
										m.user.username = p.user.username;
										p.user.username = null;
									}
									if(p.user.discriminator != null)
									{
										m.user.discriminator = p.user.discriminator;
										p.user.discriminator = null;
									}
									if(p.user.avatar != null)
									{
										m.user.avatar = p.user.avatar;
										p.user.avatar = null;
									}
									if(p.roles != null)
									{
										m.roles = p.roles;
										p.roles = null;
									}
									if(p.status.equals("offline"))
									{
										g.removePresence(p);
									} else
									{
										g.addPresence(p);
									}
									Main.scriptManager.fireEvent("PRESENCE_UPDATE", p);
								}
								break;
							case "TYPING_START":
								Channel c = Main.discordAPI.getChannel(data.get("channel_id").getAsString());
								if(c.is_private)
								{
									u = c.recipient;
								} else
								{
									u = c.getGuild().getMember(data.get("user_id").getAsString()).user;
								}
								Main.scriptManager.fireEvent("TYPING_START", new Structure[]{c, u});
								break;
							case "MESSAGE_CREATE":
							case "MESSAGE_UPDATE":
							case "MESSAGE_DELETE":
								Main.scriptManager.fireEvent(payload.t, new Gson().fromJson(data, Message.class));
								break;
						}
						break;
					case 7:
						DiscordAPI.closeWebSocket("Gateway requested reconnect.");
						DiscordAPI.getWebSocket();
						break;
					case 9:
						DiscordAPI.closeWebSocket("Resume failed.");
						DiscordAPI.getWebSocket();
						break;
					case 10:
						JsonObject d = new JsonObject();
						if(session_id.equals(""))
						{
							d.addProperty("token", Main.configuration.getString("botToken"));
							JsonObject properties = new JsonObject();
							properties.addProperty("$os", "linux");
							properties.addProperty("$browser", "SuprDiscordBot");
							properties.addProperty("$device", "SuprDiscordBot");
							properties.addProperty("$referrer", "");
							properties.addProperty("$referring_domain", "");
							d.add("properties", properties);
							d.addProperty("compress", false);
							d.addProperty("large_threshold", 50);
							d.add("shard", new JsonParser().parse("[0,1]").getAsJsonArray());
							Main.discordAPI.send(2, d);
						} else
						{
							d.addProperty("token", Main.configuration.getString("botToken"));
							d.addProperty("session_id", session_id);
							d.addProperty("seq", lastSeq);
							Main.discordAPI.send(6, d);
						}
						WebSocketHeart.interval = new JsonParser().parse(payload.d.toString()).getAsJsonObject().get("heartbeat_interval").getAsInt();
						break;
					case 11:
						WebSocketHeart.gotACK = true;
						break;
				}
			});
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void send(JsonObject json)
	{
		this.endpoint.send(json.toString());
	}

	public void close(String reason)
	{
		try
		{
			System.out.println("[WebSocket]     Manually closing: " + reason);
			this.endpoint.userSession.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
