# SuprDiscordBot Setup

This step-by-step guide will tell you how to setup your SuprDiscordBot and it is recommended you do **exactly** as it says, to avoid any problems.

1. Open a console and verify with `java -version` that you have at least Java 8 installed - [install it](https://java.com/en/download/) if missing.
2. [Download the latest release](https://github.com/timmyrs/SuprDiscordBot/releases).
3. Unzip it.
4. Move some example scripts from `scripts/examples/` into `scripts/` so the Bot has *something* to do.
5. Double click the `start.bat` or open a terminal, `cd` into the directory and then `sh start.sh`.
6. [Register an application at Discord](https://discordapp.com/developers/applications/). The only field you have to fill out is the app name.
7. Add a bot user - do not check any of the boxes - reveal and copy the **Bot Token** and **[Save changes]**.
8. Open the `config.json` in the SuprDiscordBot directory and replace `BOT_TOKEN` with your **Bot Token**.
9. Start the SuprDiscordBot again *(see step 5)*.
10. Copy the **Client ID** from your Discord Application.
11. Replace `CLIENT_ID` in the following URL with your **Client ID** and then open the URL in your browser. `https://discordapp.com/oauth2/authorize?client_id=CLIENT_ID&scope=bot&permissions=2146958463`

## That's it!

Now your bot should have joined your guild.

You can change the profile picture of your bot by changing your **App Icon**.

If you want to *Java*script your bot yourself, check out [SCRIPTING.md](https://github.com/timmyrs/SuprDiscordBot/blob/master/SCRIPTING.md).
