script.on("LOAD", function()
{
	if(discord.isInDebugMode())
	{
		script.on("MESSAGE_CREATE", function(msg)
		{
		var cont = msg.content.toLowerCase().trim();
        if(cont.substr(0, 5) == "+dump")
        {
			if(msg.getChannel().is_private)
			{

					console.log("┌ DMs");
					script.each(discord.getDMs(), function(c)
					{
						console.log("│   ├ " + c.recipient);
					});
					script.each(discord.getGuilds(), function(g)
					{
						console.log("├ " + g.name);
						console.log("│   ├ Channels");
						script.each(g.getChannels(), function(c)
						{
							console.log("│   │   ├ " + c.getName());
							script.each(c.permission_overwrites, function(o)
							{
								console.log("│   │   │    ├ " + o);
							});
						});
						console.log("│   ├ Roles");
						script.each(g.roles, function(r)
						{
							console.log("│   │   ├ " + r.name);
							script.each(permission.bitsToStrings(r.permissions), function(p)
							{
								console.log("│   │   │    ├ " + p);
							});
						});
						console.log("│   └ Members");
						script.each(g.members, function(m)
						{
							console.log("│       ├ " + m.user);
							var p = m.getPresence();
							if(p != null)
							{
								console.log("│       │   └ " + p);
								if(p.game != null)
								{
									console.log("│       │        └ " + p.game);
								}
							}
						});
					});
                } else
                {
                    msg.delete();
                }
            }
		});
	} else
	{
		console.error("You are not in debugging mode, so Debugging.js is now doing *nothing*!");
		console.info("You can start in debugging mode by adding the --debug argument.");
	}
});
