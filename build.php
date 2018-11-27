<?php
if(file_exists("SuprDiscordBot.zip"))
	unlink("SuprDiscordBot.zip");
$zip=new ZipArchive();
$zip->open("SuprDiscordBot.zip",ZipArchive::CREATE+ZipArchive::EXCL+ZipArchive::CHECKCONS)or die("Failed to create SuprDiscordBot.zip.\n");
foreach(["LICENSE","readme.url","start.bat","start.sh","SuprDiscordBot.jar"]as$f)
	$zip->addFile($f,$f);
$zip->close();
