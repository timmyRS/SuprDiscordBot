package de.timmyrs.suprdiscordbot.apis;

import java.util.ArrayList;

/**
 * Permission API ('permission')
 * Permission values taken directly from https://discordapp.com/developers/docs/topics/permissions
 *
 * @author timmyRS
 * @since 1.1
 */
@SuppressWarnings("unused")
public class PermissionAPI
{
	/**
	 * Allows creation of instant invites
	 */
	public final int CREATE_INSTANT_INVITE = 0x00000001;
	/**
	 * Allows kicking members
	 */
	public final int KICK_MEMBERS = 0x00000002;
	/**
	 * Allows banning members
	 */
	public final int BAN_MEMBERS = 0x00000004;
	/**
	 * Allows all permissions and bypasses channel permission overwrites
	 */
	public final int ADMINISTRATOR = 0x00000008;
	/**
	 * Allows management and editing of channels
	 */
	public final int MANAGE_CHANNELS = 0x00000010;
	/**
	 * Allows management and editing of the guild
	 */
	public final int MANAGE_GUILD = 0x00000020;
	/**
	 * Allows for the addition of reactions to messages
	 */
	public final int ADD_REACTIONS = 0x00000040;
	/**
	 * Allows reading messages in a channel. The channel will not appear for users without this permission
	 */
	public final int READ_MESSAGES = 0x00000400;
	/**
	 * Allows for sending messages in a channel.
	 */
	public final int SEND_MESSAGES = 0x00000800;
	/**
	 * Allows for sending of /tts messages
	 */
	public final int SEND_TTS_MESSAGES = 0x00001000;
	/**
	 * Allows for deletion of other users messages
	 */
	public final int MANAGE_MESSAGES = 0x00002000;
	/**
	 * Links sent by this user will be auto-embedded
	 */
	public final int EMBED_LINKS = 0x00004000;
	/**
	 * Allows for uploading images and files
	 */
	public final int ATTACH_FILES = 0x00008000;
	/**
	 * Allows for reading of message history
	 */
	public final int READ_MESSAGE_HISTORY = 0x00010000;
	/**
	 * Allows for using the @everyone tag to notify all users in a channel, and the @here tag to notify all online users in a channel
	 */
	public final int MENTION_EVERYONE = 0x00020000;
	/**
	 * Allows the usage of custom emojis from other servers
	 */
	public final int USE_EXTERNAL_EMOJIS = 0x00040000;
	/**
	 * Allows for joining of a voice channel
	 */
	public final int CONNECT = 0x00100000;
	/**
	 * Allows for speaking in a voice channel
	 */
	public final int SPEAK = 0x00200000;
	/**
	 * Allows for muting members in a voice channel
	 */
	public final int MUTE_MEMBERS = 0x00400000;
	/**
	 * Allows for deafening of members in a voice channel
	 */
	public final int DEAFEN_MEMBERS = 0x00800000;
	/**
	 * Allows for moving of members between voice channels
	 */
	public final int MOVE_MEMBERS = 0x01000000;
	/**
	 * Allows for using voice-activity-detection in a voice channel
	 */
	public final int USE_VAD = 0x02000000;
	/**
	 * Allows for modification of own nickname
	 */
	public final int CHANGE_NICKNAME = 0x04000000;
	/**
	 * Allows for modification of other users nicknames
	 */
	public final int MANAGE_NICKNAMES = 0x08000000;
	/**
	 * Allows management and editing of roles
	 */
	public final int MANAGE_ROLES = 0x10000000;
	/**
	 * Allows management and editing of webhooks
	 */
	public final int MANAGE_WEBHOOKS = 0x20000000;
	/**
	 * Allows management and editing of emojis
	 */
	public final int MANAGE_EMOJIS = 0x40000000;

	public String[] intToStringArray(int i)
	{
		try
		{
			ArrayList<String> perms = new ArrayList<>();
			for(String[] perm : new String[][]{
					new String[]{"MANAGE_EMOJIS", "0x40000000"},
					new String[]{"MANAGE_WEBHOOKS", "0x20000000"},
					new String[]{"MANAGE_ROLES", "0x10000000"},
					new String[]{"MANAGE_NICKNAMES", "0x08000000"},
					new String[]{"CHANGE_NICKNAME", "0x04000000"},
					new String[]{"USE_VAD", "0x02000000"},
					new String[]{"MOVE_MEMBERS", "0x01000000"},
					new String[]{"DEAFEN_MEMBERS", "0x00800000"},
					new String[]{"MUTE_MEMBERS", "0x00400000"},
					new String[]{"SPEAK", "0x00200000"},
					new String[]{"CONNECT", "0x00100000"},
					new String[]{"USE_EXTERNAL_EMOJIS", "0x00040000"},
					new String[]{"MENTION_EVERYONE", "0x00020000"},
					new String[]{"READ_MESSAGE_HISTORY", "0x00010000"},
					new String[]{"ATTACH_FILES", "0x00008000"},
					new String[]{"EMBED_LINKS", "0x00004000"},
					new String[]{"MANAGE_MESSAGES", "0x00002000"},
					new String[]{"SEND_TTS_MESSAGES", "0x00001000"},
					new String[]{"SEND_MESSAGES", "0x00000800"},
					new String[]{"READ_MESSAGES", "0x00000400"},
					new String[]{"ADD_REACTIONS", "0x00000040"},
					new String[]{"MANAGE_GUILD", "0x00000020"},
					new String[]{"MANAGE_CHANNELS", "0x00000010"},
					new String[]{"ADMINISTRATOR", "0x00000008"},
					new String[]{"BAN_MEMBERS", "0x00000004"},
					new String[]{"KICK_MEMBERS", "0x00000002"},
					new String[]{"CREATE_INSTANT_INVITE", "0x00000001"}
			})
			{
				int val = Long.decode(perm[1]).intValue();
				if((i - val) >= 0)
				{
					i -= val;
					perms.add(perm[0]);
				}
			}
			return perms.toArray(new String[perms.size()]);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
