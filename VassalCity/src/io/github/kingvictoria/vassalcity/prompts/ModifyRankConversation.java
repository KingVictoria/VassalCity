package io.github.kingvictoria.vassalcity.prompts;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.plugin.Plugin;

public class ModifyRankConversation {

	public ModifyRankConversation(Plugin plugin, CommandSender sender, StringPrompt prompt) {
        if (sender instanceof Conversable) {
            ConversationFactory conversationFactory = new ConversationFactory(plugin).withModality(true).withFirstPrompt(prompt).withEscapeSequence("/quit");
            conversationFactory.buildConversation((Conversable) sender).begin();
        }
    }
 
    public static String getPromptText() {
        return "Are you sure you want to do that? Use " + ChatColor.GREEN + "/yes " + ChatColor.WHITE + "to continue or " + ChatColor.RED + "/no " + ChatColor.WHITE + "to cancel";
    }
	
}
