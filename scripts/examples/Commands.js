// Privileged Roles:
var roles = ["Mod"];

// Handle message creation
script.on("MESSAGE_CREATE", function(msg)
{
	// Don't handle own messages.
	if(msg.author == discord.user)
	{
		return;
	}

	// Set variables
	var cont = msg.content.toLowerCase().trim(),
	channel = msg.getChannel();

	// Write message to log
	console.log(msg.author.username + " wrote " + cont + " in " + channel.getName());

	// Slowmode
	if(channel.isPartOfGuild() && msg.author)
	{
		var slowmode = channel.getValues().getInt("slowmode");
		if(slowmode > 0)
		{
			var overwrite = channel.getOverwrite(msg.author);
			if(overwrite == null)
			{
				overwrite = discord.createOverwrite().setUser(msg.author);
			}
			overwrite.deny(permission.SEND_MESSAGES);
			channel.overwritePermissions(overwrite);
			script.timeout(function()
			{
				overwrite.deny(-permission.SEND_MESSAGES);
				channel.overwritePermissions(overwrite);
			}, (slowmode * 1000));
		}
	}

	// Handle commands
	if(cont.substr(0, 7) == "+react ")
	{
		if(channel.isPartOfGuild())
		{
			msg.delete();
		}
		msg = channel.getMessage(cont.substr(7));
		if(msg != null)
		{
			msg.addReactions(["🇴", "🇰"]);
		}
	} else if(cont == "+react")
	{
		msg.addReactions(["🇴", "🇰"]);
	} else if(cont == "+info")
	{
		var presence = discord.getPresence(msg.author.id);
		if(channel.isPartOfGuild())
		{
			msg.delete();
			channel = msg.author.getDMChannel();
		}
		channel.sendTyping();
		var embed = discord.createEmbed().setColor(0xF57C00).setTitle("Info").setFooter("SuprDiscordBot, Commands.js, by timmyRS");
		embed.setDescription(presence.user);
		if(presence.game != null)
		{
			embed.addField("Game", presence.game, false);
		} else
		{
			embed.addField("Game", "None", false);
		}
		channel.sendMessage(embed);
	} else if(cont == "+help")
	{
		if(channel.isPartOfGuild())
		{
			msg.delete();
			channel = msg.author.getDMChannel();
		}
		channel.sendTyping().sendMessage(discord.createEmbed()
			.setColor(0xF57C00)
			.setTitle("SuprDiscordBot Help")
			.setURL("https://github.com/timmyrs/SuprDiscordBot")
			.addField("Everywhere-Commands", "`+react`, `+react <id>`, `+info` and `+help` work everywhere.", false)
			.addField("Guild-only", "`+explode`, `+slowmode` and `xd`-fixture are Guild-only.", false)
			.addField("Guild-Mod-only", "`+explode <id>`, `+clear <count> [<mentions ...>]`, `+erase <count>`, `+mute <secs> <mentions ...>`, `+mute <mentions ...>`, `+unmute <mentions ...>`, `+gute <mentions ...>`, `+ungute <mentions ...>` and `+slowmode <secs>` are only available on Guilds for Mods.", false)
			.setFooter("SuprDiscordBot, Commands.js, by timmyRS")
			);
	} else if(channel.isPartOfGuild())
	{
		// Handle severe cases of incompetence
		if(msg.content.indexOf("xd") != -1)
		{
			msg.delete();
			msg.author.getDMChannel().sendMessage("Here is your message, but with fixed `xD`:\n```\n" + msg.content.split("xd").join("xD") + "\n```");
			return;
		}

		// Find out if member is privileged
		var hasperm = false;
		script.each(channel.getGuild().getMember(msg.author).getRoles(), function(role)
		{
			script.each(roles, function(name)
			{
				if(role.name == name)
				{
					hasperm = true;
				}
			});
		});

		// Guild-only commands
		if(cont.substr(0, 6) == "+mute " || cont.substr(0, 6) == "+gute ")
		{
			if(hasperm)
			{
				channel.sendTyping();
				msg.delete();
				var gmute = cont.substr(0, 6) == "+gute ", secs = parseInt(cont.substr(6).split(" ")[0]);
				if(isNaN(secs))
				{
					secs = 0;
				}
				if(secs > 600)
				{
					var msg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Won't temp-mute for more than 10 minutes. Use `+mute <mentions ...>` or `+gute <mentions ...>` (without `<secs>`) instead.");
					script.timeout(function()
					{
						msg.delete();
					}, 5000);
				} else
				{
					script.each(msg.mentions, function(mention)
					{
						var msg;
						script.each((gmute ? channel.getGuild().getTextChannels() : [channel]), function(c)
						{
							var overwrite = c.getOverwrite(mention);
							if(overwrite == null)
							{
								overwrite = discord.createOverwrite().setUser(mention);
							}
							overwrite.deny(permission.SEND_MESSAGES);
							c.overwritePermissions(overwrite);
							if(secs != 0)
							{
								script.timeout(function()
								{
									overwrite.deny(-permission.SEND_MESSAGES);
									c.overwritePermissions(overwrite);
								}, (secs * 1000));
							}
						});
						if(secs != 0)
						{
							script.timeout(function()
							{
								msg.edit("~~" + msg.content + "~~ **[Expired]**");
							}, (secs * 1000));
						}
						if(secs == 0)
						{
							if(gmute)
							{
								channel.sendMessage(":thumbsup: " + mention.getHandle() + " is now muted in " + channel.getGuild().name + ".");
							} else
							{
								channel.sendMessage(":thumbsup: " + mention.getHandle() + " is now muted in " + channel.getHandle() + ".");
							}
						} else
						{
							if(gmute)
							{
								msg = channel.sendMessage(":thumbsup: " + mention.getHandle() + " is now muted for **" + secs + "** second" + (secs == 1 ? "" : "s") +" in " + channel.getGuild().name + ".");
							} else
							{
								msg = channel.sendMessage(":thumbsup: " + mention.getHandle() + " is now muted for **" + secs + "** second" + (secs == 1 ? "" : "s") +" in " + channel.getHandle() + ".");
							}
						}
					});
				}
			}
		}
		else if(cont.substr(0, 8) == "+unmute " || cont.substr(0, 8) == "+ungute ")
		{
			if(hasperm)
			{
				channel.sendTyping();
				msg.delete();
				var gmute = cont.substr(0, 8) == "+ungute ", msgs = [];
				script.each(msg.mentions, function(mention)
				{
					script.each((gmute ? channel.getGuild().getTextChannels() : [channel]), function(c)
					{
						var overwrite = c.getOverwrite(mention);
						if(overwrite == null)
						{
							overwrite = discord.createOverwrite().setUser(mention);
						}
						overwrite.deny(-permission.SEND_MESSAGES);
						c.overwritePermissions(overwrite);
					});
					if(gmute)
					{
						channel.sendMessage(":thumbsup: " + mention.getHandle() + " is no longer muted in " + channel.getGuild().name + ".");
					} else
					{
						channel.sendMessage(":thumbsup: " + mention.getHandle() + " is no longer muted in " + channel.getHandle() + ".");
					}
				});
			}
		}
		else if(cont == "+explode" || cont.substr(0, 9) == "+explode ")
		{
			channel.sendTyping();
			if(!hasperm && cont.substr(0, 9) == "+explode ")
			{
				mymsg = channel.sendMessage(":warning: Only privileged members may use `+explode <id>`.");
				script.timeout(function()
				{
					mymsg.delete();
				}, 5000);
				return;
			}
			var delmsg = mymsg = null;
			if(cont.substr(0, 9) == "+explode ")
			{
				msg.delete();
				delmsg = channel.getMessage(cont.substr(9));
				if(delmsg != null)
				{
					delmsg = delmsg.id;
				}
			} else
			{
				delmsg = msg.id;
			}
			if(delmsg == null)
			{
				mymsg = channel.sendMessage(":warning: `" + cont.substr(9) + "` was not found in " + channel.getHandle() + "...");
				script.timeout(function()
				{
					mymsg.delete();
				}, 5000);
			} else
			{
				mymsg = channel.sendMessage("Message **" + delmsg + "** explodes in `5`...");
				script.timeout(function()
				{
					mymsg.edit("Message **" + delmsg + "** explodes in `4`...");
					script.timeout(function()
					{
						mymsg.edit("Message **" + delmsg + "** explodes in `3`...");
						script.timeout(function()
						{
							mymsg.edit("Message **" + delmsg + "** explodes in `2`...");
							script.timeout(function()
							{
								mymsg.edit("Message **" + delmsg + "** explodes in `1`...");
								script.timeout(function()
								{
									channel.deleteMessages([delmsg, mymsg.id]);
								}, 1000);
							}, 1000);
						}, 1000);
					}, 1000);
				}, 1000);
			}
		} else if(cont.substr(0, 7) == "+clear ")
		{
			channel.sendTyping();
			msg.delete();
			var count = parseInt(cont.substr(7)), mymsg;
			if(!hasperm)
			{
				msg.delete();
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Only privileged members may use `+clear <count> [<mentions ...>]`.");
			} else if(count < 1)
			{
				msg.delete();
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Can't delete 0 (or less) messages.");
			} else if(count == 1)
			{
				msg.delete();
				mymsg = channel.sendMessage(":thumbsdown: Now you are just being lazy, " + msg.author.getHandle() + ".");
			} else
			{
				var deleted = 0, mentions = [];
				script.each(msg.mentions, function(user)
				{
					mentions.push(user.id);
				});
				while(deleted < count)
				{
					var deletemsgs = [];
					script.each(channel.getMessages(100), function(msg)
					{
						if(!msg.pinned && deleted < count)
						{
							if(mentions.length > 0)
							{
								if(script.inArray(mentions, msg.author.id))
								{
									deletemsgs.push(msg.id);
									deleted++;
								}
							}
							else
							{
								deletemsgs.push(msg.id);
								deleted++;
							}
						}
					});
					channel.deleteMessages(deletemsgs);
				}
				mymsg = channel.sendMessage(":thumbsup: Deleted a whopping **" + deleted + "** messages.");
			}
			script.timeout(function()
			{
				mymsg.delete();
			}, 5000);
		} else if(cont.substr(0, 7) == "+erase ")
		{
			channel.sendTyping();
			var count = parseInt(cont.substr(7)), mymsg;
			msg.delete();
			if(!hasperm)
			{
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Only privileged members may use `+erase <count>`.");
			} else if(count < 1)
			{
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Can't delete 0 or less messages.");
			} else
			{
				var i = count, totaldel = 0;
				mymsg = channel.sendMessage(":warning: Deleting **" + count + "** messages...");
				while(i > 0)
				{
					var reading = 99;
					if(i < 99)
					{
						reading = i;
					}
					var deleted = 0;
					script.each(channel.getMessagesBefore(reading, mymsg.id), function(msg)
					{
						if(!msg.pinned)
						{
							if(msg.id != mymsg.id)
							{
								msg.delete();
								i--;
								deleted++;
								totaldel++;
							}
						}
					});
					if(deleted == 0)
					{
						i = 0;
					} else
					{
						mymsg.edit(":warning: Deleting **" + i + "** messages...");
					}
				}
				mymsg.edit(":thumbsup: Manually deleted a whopping **" + totaldel + "** messages.");
			}
			script.timeout(function()
			{
				mymsg.delete();
			}, 5000);
		} else if(cont == "+slowmode")
		{
			channel.sendTyping();
			msg.delete();
			var mymsg;
			if(channel.getValues().has("slowmode"))
			{
				mymsg = channel.sendMessage(":information_source: An interval of **" + channel.getValues().getString("slowmode") + "** second(s) is being inforced in " + channel.getHandle() + ".");
			} else
			{
				mymsg = channel.sendMessage(":information_source: There is no interval being enforced in " + channel.getHandle() + ".");
			}
			script.timeout(function()
			{
				mymsg.delete();
			}, 5000);
		} else if(cont.substr(0, 10) == "+slowmode ")
		{
			channel.sendTyping();
			msg.delete();
			var secs = parseInt(cont.substr(10)), mymsg;
			if(!hasperm)
			{
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Only privileged members may use `+clear <count>`.");
			} else if(secs == 0)
			{
				channel.getValues().unset("slowmode");
				mymsg = channel.sendMessage(":thumbsup: Disabled slowmode in " + channel.getHandle() + ".");
			} else if(secs < 0)
			{
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Interval has to be a positive integer.");
			} else if(secs > 60)
			{
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Won't set interval above 60 seconds.");
			} else
			{
				channel.getValues().set("slowmode", secs);
				mymsg = channel.sendMessage(":thumbsup: An interval of **" + secs + "** second(s) between messages is now being enforced.");
			}
			script.timeout(function()
			{
				mymsg.delete();
			}, 5000);
		} else if(cont.substr(0, 11) == "+timeclear ")
		{
			channel.sendTyping();
			msg.delete();
			var hours = parseInt(cont.substr(11)), mymsg;
			if(!hasperm)
			{
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Only privileged members may use `+timeclear <hours>`.");
			} else if(hours < 0)
			{
				mymsg = channel.sendMessage(":warning: " + msg.author.getHandle() + " Hours have to be a positive integer.");
			} else
			{
				var msgs, before = channel.last_message_id, done = false, count = 0;
				while(!done && (msgs = channel.getMessagesBefore(100, before)).length != 0)
				{
					var deleteids = [];
					script.each(msgs, function(msg)
					{
						if(done)
						{
							return;
						}
						before = msg.id;
						if(msg.getTime() < (script.time() - (hours * 3600)))
						{
							if(msg.getTime() < (script.time() - 1209600))
							{
								console.info("Can't delete messages older than 2 weeks. Use +erase <count> instead.");
								done = true;
							} else
							{
								deleteids.push(msg.id);
								count++;
							}
						}
					});
					channel.deleteMessages(deleteids);
				}
				mymsg = channel.sendMessage(":thumbsup: Deleted a whopping **" + count + "** messages.");
			}
			script.timeout(function()
			{
				mymsg.delete();
			}, 5000);
		}
	}
}).on("MESSAGE_UPDATE", function(msg)
{
	// Also handle updated messages.
	if(msg.content != null)
	{
		script.fireEvent("MESSAGE_CREATE", msg);
	}
});
