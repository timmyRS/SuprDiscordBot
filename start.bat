@echo off
title SuprDiscordBot

:a
	if exist latest.log (
		if exist previous.log (
			del previous.log
		)
		move latest.log previous.log
	)
	java -jar SuprDiscordBot.jar %*
goto a
