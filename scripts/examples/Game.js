// The name of the game that is to be displayed on Discord:
var game = "https://github.com/timmyrs/SuprDiscordBot";

script.on("connected", function() // Called everytime the WebSocket to Discord has been (re)opened.
{
	// Sends Status Update (OP 3) with custom game name.
	discord.send(3, '{"status":"online","game":{"type":0,"name":"'+game+'"},"afk":false,"since":0}');
	// Note for "status": It can be "online", "away", "dnd" or "invisible".
}).on("load", function()
{
	script.fireEvent("connected"); // Firing connected event on load.
});
