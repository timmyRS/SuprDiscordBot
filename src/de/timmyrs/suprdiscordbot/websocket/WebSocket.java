package de.timmyrs.suprdiscordbot.websocket;

import com.google.gson.JsonObject;
import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.apis.DiscordAPI;
import de.timmyrs.suprdiscordbot.scripts.ScriptWatcher;
import de.timmyrs.suprdiscordbot.structures.Channel;
import de.timmyrs.suprdiscordbot.structures.GatewayPayload;
import de.timmyrs.suprdiscordbot.structures.Guild;
import de.timmyrs.suprdiscordbot.structures.Member;
import de.timmyrs.suprdiscordbot.structures.Message;
import de.timmyrs.suprdiscordbot.structures.Presence;
import de.timmyrs.suprdiscordbot.structures.User;
import de.timmyrs.suprdiscordbot.structures.VoiceState;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class WebSocket
{
	static int lastSeq;
	private static String session_id = "";

	public WebSocket(String url) throws URISyntaxException, IOException, DeploymentException
	{
		Main.webSocketEndpoint = new WebSocketEndpoint(new URI(url), message->
		{
			JsonObject json = Main.jsonParser.parse(message).getAsJsonObject();
			GatewayPayload payload = Main.gson.fromJson(json, GatewayPayload.class);
			switch(payload.op)
			{
				default:
					if(Main.debug)
					{
						Main.log("Socket", "Unhandled Operation: " + json);
					}
					break;
				case 0:
					lastSeq = payload.s;
					final JsonObject data = json.get("d").getAsJsonObject();
					switch(payload.t)
					{
						default:
							if(Main.debug)
							{
								Main.log("Socket", "Unhandled Event " + payload.t + ": " + json.get("d").toString());
							}
							break;
						case "READY":
						{
							final JsonObject d = json.get("d").getAsJsonObject();
							session_id = d.get("session_id").getAsString();
							DiscordAPI.guilds.clear();
							Main.discordAPI.user = Main.gson.fromJson(d.get("user"), User.class);
							if(!Main.ready)
							{
								new ScriptWatcher();
								Main.ready = true;
							}
							Main.scriptManager.fireEvent("CONNECTED");
							break;
						}
						case "GUILD_CREATE":
						{
							final Guild g = Main.gson.fromJson(data, Guild.class);
							for(Channel c : g.getChannels())
							{
								c.guild_id = g.id;
							}
							for(Member member : g.members)
							{
								member.guild_id = g.id;
							}
							for(Presence presence : g.presences)
							{
								presence.guild_id = g.id;
								presence.user = g.getMember(presence.user.id).user;
							}
							for(VoiceState vs : g.voice_states)
							{
								vs.guild_id = g.id;
							}
							DiscordAPI.guilds.add(g);
							Main.scriptManager.fireEvent("GUILD_CREATE", g);
							break;
						}
						case "GUILD_DELETE":
						{
							final Guild g = Main.discordAPI.getGuild(data.get("id").getAsString());
							DiscordAPI.guilds.remove(g);
							Main.scriptManager.fireEvent("GUILD_DELETE", g);
							break;
						}
						case "GUILD_MEMBER_ADD":
						{
							final Member m = Main.gson.fromJson(data, Member.class);
							m.getGuild().addMember(m);
							Main.scriptManager.fireEvent("USER_JOIN", m);
							break;
						}
						case "GUILD_MEMBER_REMOVE":
						{
							final Presence p = Main.gson.fromJson(data, Presence.class);
							p.getGuild().removeMember(p.user.id);
							p.getGuild().removePresence(p.user.id);
							Main.scriptManager.fireEvent("USER_REMOVE", p);
							break;
						}
						case "PRESENCE_UPDATE":
						{
							final Guild g = Main.discordAPI.getGuild(data.get("guild_id").getAsString());
							final Presence p = Main.gson.fromJson(data, Presence.class);
							final Presence cp = g.getPresence(p.user.id);
							final Member m = g.getMember(p.user.id);
							if(cp == null)
							{
								if(!p.status.equals("offline"))
								{
									p.user = m.user;
									g.addPresence(p);
									Main.scriptManager.fireEvent("PRESENCE_GO_ONLINE", p);
								}
								break;
							}
							if(p.status.equals("offline"))
							{
								Main.scriptManager.fireEvent("PRESENCE_GO_OFFLINE", g.getPresence(p.user.id));
								g.removePresence(cp.user.id);
								break;
							}
							if(p.user.username == null || p.user.discriminator == null || p.user.avatar == null)
							{
								p.user = cp.user;
							}
							else
							{
								cp.user = p.user;
							}
							if(!p.status.equals(cp.status))
							{
								Main.scriptManager.fireEvent("PRESENCE_UPDATE_STATUS", new Object[]{p, cp.status});
								cp.status = p.status;
							}
							if(cp.game == null)
							{
								if(p.game != null)
								{
									Main.scriptManager.fireEvent("PRESENCE_UPDATE_GAME", new Object[]{p, null});
									cp.game = p.game;
								}
							}
							else
							{
								if(p.game == null)
								{
									Main.scriptManager.fireEvent("PRESENCE_UPDATE_GAME", new Object[]{p, cp.game});
									p.game = null;
								}
								else if(!cp.game.name.equals(p.game.name))
								{
									Main.scriptManager.fireEvent("PRESENCE_UPDATE_GAME", new Object[]{p, cp.game});
									cp.game = p.game;
								}
							}
							if(!cp.user.username.equals(p.user.username) || !cp.user.username.equals(p.user.discriminator))
							{
								Main.scriptManager.fireEvent("PRESENCE_UPDATE_USER", new Object[]{p, cp.user});
								p.user = cp.user;
							}
							g.addPresence(cp);
							break;
						}
						case "GUILD_MEMBER_UPDATE":
						{
							final Member m = Main.gson.fromJson(data, Member.class);
							final Guild g = m.getGuild();
							final Member cm = m.getGuild().getMember(m.user.id);
							if(cm.nick == null)
							{
								if(m.nick != null)
								{
									Main.scriptManager.fireEvent("MEMBER_UPDATE_NICK", new Object[]{m, null});
									cm.nick = m.nick;
								}
							}
							else
							{
								if(m.nick == null)
								{
									Main.scriptManager.fireEvent("MEMBER_UPDATE_NICK", new Object[]{m, cm.nick});
									cm.nick = null;
								}
								else if(!cm.nick.equals(m.nick))
								{
									Main.scriptManager.fireEvent("MEMBER_UPDATE_NICK", new Object[]{m, cm.nick});
									cm.nick = m.nick;
								}
							}
							if(!Arrays.equals(cm.roles, m.roles))
							{
								Main.scriptManager.fireEvent("MEMBER_UPDATE_ROLES", new Object[]{m, cm.roles});
							}
							g.addMember(m);
							break;
						}
						case "TYPING_START":
						{
							final User u;
							final Channel c = Main.discordAPI.getChannel(data.get("channel_id").getAsString());
							if(c.type == 1)
							{
								u = c.recipients[0];
							}
							else if(c.type == 3)
							{
								u = Main.discordAPI.getUser(data.get("user_id").getAsString());
							}
							else
							{
								u = c.getGuild().getMember(data.get("user_id").getAsString()).user;
							}
							Main.scriptManager.fireEvent("TYPING_START", new Object[]{c, u});
							break;
						}
						case "CHANNEL_UPDATE":
						{
							final Guild g = Main.discordAPI.getGuild(data.get("guild_id").getAsString());
							final Channel c = Main.gson.fromJson(data, Channel.class);
							Channel _c = g.getChannel(c.id);
							Main.scriptManager.fireEvent("CHANNEL_UPDATE", new Object[]{c, _c});
							if(!_c.getName().equals(c.getName()))
							{
								Main.scriptManager.fireEvent("CHANNEL_UPDATE_NAME", new Object[]{c, _c.getName()});
							}
							if(c.topic != null && !_c.topic.equals(c.topic))
							{
								Main.scriptManager.fireEvent("CHANNEL_UPDATE_TOPIC", new Object[]{c, _c.topic});
							}
							if(_c.position != c.position)
							{
								Main.scriptManager.fireEvent("CHANNEL_UPDATE_POSITION", new Object[]{c, _c.position});
							}
							if(c.permission_overwrites != null && !Arrays.equals(_c.permission_overwrites, c.permission_overwrites))
							{
								Main.scriptManager.fireEvent("CHANNEL_UPDATE_OVERWRITES", new Object[]{c, _c.permission_overwrites});
							}
							g.addChannel(c);
							break;
						}
						case "MESSAGE_CREATE":
							final Message msg = Main.gson.fromJson(data, Message.class);
							final Channel c = msg.getChannel();
							if(c.isPartOfAGuild())
							{
								Guild g = c.getGuild();
								c.last_message_id = msg.id;
								g.addChannel(c);
							}
						case "MESSAGE_UPDATE":
						case "MESSAGE_DELETE":
							Main.scriptManager.fireEvent(payload.t, Main.gson.fromJson(data, Message.class));
							break;
					}
					break;
				case 7:
					Main.log("Socket", "Gateway requested reconnect.");
					System.exit(0);
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
						d.add("shard", Main.jsonParser.parse("[0,1]").getAsJsonArray());
						Main.discordAPI.send(2, d);
					}
					else
					{
						d.addProperty("token", Main.configuration.getString("botToken"));
						d.addProperty("session_id", session_id);
						d.addProperty("seq", lastSeq);
						Main.discordAPI.send(6, d);
					}
					WebSocketHeart.interval = Main.jsonParser.parse(payload.d.toString()).getAsJsonObject().get("heartbeat_interval").getAsInt();
					break;
				case 11:
					WebSocketHeart.gotACK = true;
					break;
			}
		});
	}
}
