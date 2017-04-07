// Log guild creation
// Note: The guild create event is fired when we are added to a guild or the websocket connection started
script.on("GUILD_CREATE", function(guild)
{
	console.log("GUILD_CREATE " + guild.toString());
})
// Log guild deletion
// Note: The guild delete event is fired when we are kicked, the guild goes offline or is actually being deleted
.on("GUILD_DELETE", function(guild)
{
	console.log("GUILD_DELETE " + guild.toString());
});
