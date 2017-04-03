// Privileged Roles:
var roles = ["Mod"];

// Handle message creation
script.on("MESSAGE_CREATE", function(msg)
{
	// Set variables
	var cont = msg.content.toLowerCase().trim(),
	channel = msg.getChannel();

	// Don't handle own messages.
	if(msg.author.id == discord.user.id)
	{
		return;
	}

	// Write message to log
	console.log(msg.author.username + " wrote " + cont + " in " + channel.getName());

	// Slowmode
	if(!channel.is_private)
	{
		var slowmode = channel.getValues().getInt("slowmode");
		if(slowmode > 0)
		{
			var lastmsg = msg.author.getValues().getInt("lastmsg");
			if((lastmsg + slowmode) > script.time())
			{
				msg.delete();
				msg.author.getDMChannel().sendMessage("Please wait **" + slowmode + "** seconds in between " + channel.getName() + "-messages. Your message:\n```\n" + msg.content + "\n```");
				return;
			}
			msg.author.getValues().set("lastmsg", script.time());
		}
	}

	// Handle commands
	if(cont == "+hello")
	{
		channel.sendTyping().sendMessage("Hello, world!");
	} else if(cont.substr(0, 7) == "+react ")
	{
		if(!channel.is_private)
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
	} else if(cont == "+help")
	{
		if(!channel.is_private)
		{
			msg.delete();
			channel = msg.author.getDMChannel();
		}
		channel.sendTyping().sendMessage(discord.createEmbed()
			.setColor(0xF57C00)
			.setTitle("SuprDiscordBot Help")
			.setURL("https://github.com/timmyrs/SuprDiscordBot")
			.addField("Everywhere-Commands", "`+react`, `+react <id>` and `+hello` work everywhere.", false)
			.addField("Guild-only", "`+explode` and `xd`-fixture are Guild-only.", false)
			.addField("Guild-Mod-only", "`+explode <id>`, `+clear <count>` and `+slowmode <secs>` are only available on Guilds for Mods.", false)
			.setFooter("SuprDiscordBot, Commands.js, by timmyRS")
			);
	} else if(channel.is_private)
	{
		// Write "unknown command" if command is not handled and this is a private chat.
		channel.sendTyping().sendMessage("Unknown command. Try `+help`.");
	} else if(!channel.is_private)
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
		script.each(channel.getGuild().getMember(msg.author).roles, function(role)
		{
			script.each(roles, function(name)
			{
				if(role.name == name)
				{
					hasperm = true;
				}
			});
		});

		if(cont == "+explode" || cont.substr(0, 9) == "+explode ")
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
			var count = parseInt(cont.substr(7)), mymsg;
			if(!hasperm)
			{
				msg.delete();
				mymsg = channel.sendMessage(":warning: Only privileged members may use `+clear <count>`.");
			} else if(count < 1)
			{
				msg.delete();
				mymsg = channel.sendMessage(":warning: Can't delete 0 or less messages.");
			} else if(count == 1)
			{
				msg.delete();
				mymsg = channel.sendMessage(":thumbsdown: Now you are just being lazy, " + msg.author.getHandle() + ".");
			} else if(count > 99)
			{
				msg.delete();
				mymsg = channel.sendMessage(":warning: Won't delete more than 99 messages.");
			} else
			{
				var deletemsgs = [];
				script.each(channel.getMessages(count + 1), function(msg)
				{
					deletemsgs.push(msg.id);
				});
				channel.deleteMessages(deletemsgs);
				mymsg = channel.sendMessage(":thumbsup: Deleted a whopping **" + count + "** messages.");
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
				mymsg = channel.sendMessage(":warning: Only privileged members may use `+clear <count>`.");
			} else if(secs == 0)
			{
				channel.getValues().unset("slowmode");
				mymsg = channel.sendMessage(":thumbsup: Disabled slowmode in " + channel.getHandle() + ".");
			} else if(secs < 0)
			{
				mymsg = channel.sendMessage(":warning: Interval has to be a positive integer.");
			} else if(secs > 60)
			{
				mymsg = channel.sendMessage(":warning: Won't set interval above 60 seconds.");
			} else
			{
				channel.getValues().set("slowmode", secs);
				mymsg = channel.sendMessage(":thumbsup: An interval of **" + secs + "** seconds between messages is now being enforced.");
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
