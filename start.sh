#!/bin/bash


while true; do
	if [ -f latest.log ]; then
		if [ -f previous.log ]; then
			rm previous.log
		fi
		mv latest.log previous.log
	fi
	java -jar SuprDiscordBot.jar "$@"
done
