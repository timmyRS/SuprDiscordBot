// Call guild events on load, too.
script.on("load", function()
{
	script.each(discord.guilds, function(guild)
	{
		this.fireEvent("GUILD_CREATE", guild);
	});
}) // Log guild creation
.on("GUILD_CREATE", function(guild)
{
	console.log("GUILD_CREATE " + guild.toString());
}) // Log guild deletion
// Note: The delete event is fired when we are kicked, the guild goes offline or is actually being deleted
.on("GUILD_DELETE", function(guild)
{
	console.log("GUILD_DELETE " + guild.toString());
});
