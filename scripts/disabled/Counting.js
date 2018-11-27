// If your guild has a #counting channel, this will help you keep in the right order.

script.on("MESSAGE_CREATE", function(msg)
{
	var cont = msg.content.toLowerCase().trim(),
	channel = msg.getChannel();

	if(channel.getName() == "#counting")
	{
		var bool = false, last = null;
		script.each(channel.getMessages(50), function(m)
		{
			if(bool)
			{
				last = m, bool = false;
			} else if(last == null)
			{
				if(m.id == msg.id)
				{
					bool = true;
				}
			}
		});
		if(last != null)
		{
			num = parseInt(msg.content),
			lastNum = parseInt(last.content);
			if(num != (lastNum + 1))
			{
				msg.delete();
			}
		}
	}
}).on("MESSAGE_UPDATE", function(msg)
{
	if(msg.content != null)
	{
		script.fireEvent("MESSAGE_CREATE", msg);
	}
})
