// The name of the game that is to be displayed on Discord:
var game = "https://github.com/timmyrs/SuprDiscordBot";

script.on("connected", function() // Called everytime the WebSocket to Discord has been (re)opened.
{
	// Sends Status Update (OP 3) with custom game name.
	discord.send(3, '{"idle_since":0,"game":{"type":0,"name":"' + game + '"}}');
}).on("load", function()
{
	script.fireEvent("connected"); // Firing connected event on load.
});
