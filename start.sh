#!/bin/bash

if [ -f SuprDiscordBot.jar ]; then
	rm SuprDiscordBot.jar
fi
while true
do
	wget https://raw.githubusercontent.com/timmyrs/SuprDiscordBot/master/SuprDiscordBot.jar
	if [ -f latest.log ]; then
		if [ -f previous.log ]; then
			rm previous.log
		fi
		mv latest.log previous.log
	fi
	java -jar SuprDiscordBot.jar "$@"
	rm SuprDiscordBot.jar
done
