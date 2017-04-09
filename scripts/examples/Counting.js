// If your guild has a #counting channel by any chance, this will help it keep in the right order and even count with you.

var thread = undefined;
script.on("MESSAGE_CREATE", function(msg)
{
	var cont = msg.content.toLowerCase().trim(),
	channel = msg.getChannel();

	if(channel.getName() == "#counting")
	{
		num = getNum(cont);
		lastNum = getNum(channel.getMessages(2)[1].content);
		if(num == (lastNum + 1))
		{
			if(msg.author.id != discord.user.id)
			{
				if(thread !== undefined && !thread.isInterrupted())
				{
					thread.interrupt();
				}
				channel.sendTyping();
				thread = script.timeout(function()
				{
					channel.sendMessage(num + 1);
				}, 4000);
			}
		} else
		{
			msg.delete();
		}
	}
});

function getNum(cont)
{
	var num = "", counting = true;
	script.each(cont.split(""), function(char)
	{
		if(counting)
		{
			if(script.inArray(["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"], char))
			{
				num += char;
			} else
			{
				counting = false;
			}
		}
	});
	return parseInt(num);
}