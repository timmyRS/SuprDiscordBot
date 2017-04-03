script.on("USER_JOIN", function(p)
{
	console.log(p.user.username + " joined " + p.getGuild().name);
}).on("USER_LEAVE", function(p)
{
	console.log(p.user.username + " left " + p.getGuild().name);
}).on("PRESENCE_UPDATE", function(p) // Called upon update of of a presence
{
	var guild = p.getGuild(); // Get the guild this presence is part of
	console.log(guild.getMember(p).getName() + " is now " + p.status); // Write new status to console
}).on("TYPING_START", function(arr) // Called upon typing start. Note: There is NO typing stop event.
{
	var channel = arr[0], user = arr[1];
	if(channel.is_private)
	{
		console.log(user.username + " is now typing in private");
	} else
	{
		console.log(user.username + " is now typing in " + channel.getName() + " in " + channel.getGuild().name);
	}
});
