var nickname = "Tims Schrott"; // Nickname to be used on all servers.

script.on("load", function() // Called upon (re)load of this script.
{
	script.each(discord.guilds, function(guild) // Foreaches through each of the bots guilds/servers.
	{
		guild.setNickname(nickname == "" ? null : nickname); // Set name; null if name is empty.
	});
});
