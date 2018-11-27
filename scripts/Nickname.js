var nickname = "SuprDiscordBot"; // Nickname to be used on all servers.

script.on("LOAD", function() // On loading of this script...
{
	script.each(discord.guilds, function(guild) // ...for each guild we are in...
	{
		script.fireEvent("GUILD_CREATE", guild); // ...fire the GUILD_CREATE event.
	})
}).on("GUILD_CREATE", function(guild) // Called when we are added to a guild.
{
	guild.setNickname(nickname == "" ? null : nickname); // Set name; null if name is empty.
})
