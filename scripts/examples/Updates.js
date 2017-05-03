script.on("USER_JOIN", function(m) // Called when a user joins a guild
{
	console.log(m.user.getTag() + " joined " + m.getGuild().name);
}).on("USER_REMOVE", function(p) // Called when a user leaves or gets kicked out of a guild
{
	console.log(p.user.getTag() + " left " + p.getGuild().name);
}).on("PRESENCE_GO_ONLINE", function(p) // Called when a presence goes online
{
	console.log(p.user.getTag() + " is now online as " + p.status);
}).on("PRESENCE_GO_OFFLINE", function(p) // Called when a presence goes offline
{
	console.log(p.user.getTag() + " is now offline (was " + p.status + ")");
}).on("PRESENCE_UPDATE_STATUS", function(arr) // Called upon update of of a presence's status
{
	var p = arr[0];
	console.log(p.user.getTag() + " is now " + p.status + " (was " + arr[1] + ")"); // Log new and old status to console
}).on("MEMBER_UPDATE_NICK", function(arr) // Called upon update of a member's nick
{
	var m = arr[0];
	console.log(m.user.getTag() + " changed their nick from '" + (arr[1] == null ? "" : arr[1]) + "' to '" + (m.nick == null ? "" : m.nick) + "'"); // Log new and old nick to console
}).on("PRESENCE_UPDATE_GAME", function(arr) // Called upon update of a member's nick
{
	var p = arr[0];
	console.log(p.user.getTag() + " changed their game from '" + (arr[1] == null ? "" : arr[1].name) + "' to '" + (p.game == null ? "" : p.game.name) + "'"); // Log new and old game to console
}).on("MEMBER_UPDATE_ROLES", function(arr) // Called upon update of a members's guilds
{
	var m = arr[0], g = m.getGuild();
	script.each(m.roles, function(role)
	{
		if(script.inArray(arr[1], role))
		{
			console.log(m.getName() + " is now part of " + g.getRole(role).name + " in " + g.name); // Log added roles to console
		}
	});
	script.each(arr[1], function(role)
	{
		if(script.inArray(m.roles, role))
		{
			console.log(m.getName() + " is no longer part of " + g.getRole(role).name + " in " + g.name); // Log removed roles to console
		}
	});
}).on("CHANNEL_UPDATE_NAME", function(arr) // Called upon update of a channel's name
{
    var c = arr[0], old_name = arr[1];
    console.log(c.getName() + " in " + c.getGuild().name + " was called '" + old_name + "' just a second ago.");
}).on("CHANNEL_UPDATE_TOPIC", function(arr) // Called upon update of a channel's name
{
    var c = arr[0], old_topic = arr[1];
    console.log(c.getName() + " in " + c.getGuild().name + " now has a new topic.");
}).on("TYPING_START", function(arr) // Called upon typing start. Note: There is NO typing stop event.
{
	var channel = arr[0], user = arr[1];
	if(channel.is_private)
	{
		console.log(user.getTag() + " is now typing in private");
	} else
	{
		console.log(user.getTag() + " is now typing in " + channel.getName() + " in " + channel.getGuild().name);
	}
});
