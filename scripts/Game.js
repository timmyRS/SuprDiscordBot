// The name of the game that is to be displayed on Discord:
var game = "https://hax.to/sdb";

script.on("connected", function() // Called everytime the WebSocket to Discord has been (re)opened.
{
	// Sends Status Update (OP 3) with custom game name.
	discord.send(3, '{"status":"online","game":{"type":0,"name":"' + game + '"},"afk":false,"since":0}');
	// "status" can be "online", "away", "dnd" or "invisible"
	// "type" can be 0 (playing), 1 (listening to), 2 (streaming), or 3 (watching)
}).on("load", function()
{
	script.fireEvent("connected"); // Firing connected event on load.
});
