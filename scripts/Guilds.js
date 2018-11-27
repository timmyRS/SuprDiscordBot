script.on("LOAD", function() // On loading of this script...
{
	script.each(discord.guilds, function(guild) // ...for each guild we are in...
	{
		script.fireEvent("GUILD_CREATE", guild); // ...fire the GUILD_CREATE event.
	})
}).on("GUILD_CREATE", function(guild) // Fired when we are added to a guild
{
	console.log("GUILD_CREATE " + guild.toString());
}).on("GUILD_DELETE", function(guild) // Fired when we are kicked, the guild goes offline or actually being deleted
{
	console.log("GUILD_DELETE " + guild.toString());
});
