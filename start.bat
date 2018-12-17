@echo off
title SuprDiscordBot

if exist SuprDiscordBot.jar (
	del SuprDiscordBot.jar
)
:a
	powershell -Command "Invoke-WebRequest https://raw.githubusercontent.com/timmyrs/SuprDiscordBot/master/SuprDiscordBot.jar -UseBasicParsing -OutFile SuprDiscordBot.jar"
	if exist latest.log (
		if exist previous.log (
			del previous.log
		)
		move latest.log previous.log
	)
	java -jar SuprDiscordBot.jar %*
	del SuprDiscordBot.jar
goto a
