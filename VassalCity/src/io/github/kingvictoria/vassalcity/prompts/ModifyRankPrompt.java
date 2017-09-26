package io.github.kingvictoria.vassalcity.prompts;

import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import io.github.kingvictoria.vassalcity.city.City;
import io.github.kingvictoria.vassalcity.city.Permissions;
import io.github.kingvictoria.vassalcity.city.Rank;
import io.github.kingvictoria.vassalcity.main.VassalCity;
import net.md_5.bungee.api.ChatColor;

public class ModifyRankPrompt extends StringPrompt {
	
	City city;
	Rank rank;
	boolean add = false, remove = false;;
	
	public ModifyRankPrompt(City city, Rank rank){
		super();
		this.city = city;
		this.rank = rank;
	}

	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {
		if(add){
			for(String perm: Permissions.getPerms())
				if(perm.equals(answer)){
					rank.addPerm(perm);
					return null;
				}
			con.getForWhom().sendRawMessage(ChatColor.LIGHT_PURPLE+answer+ChatColor.YELLOW+" is not a valid permission");
			return null;
		}
		
		if(remove){
			for(String perm: rank.getPerms())
				if(perm.equals(answer)){
					rank.removePerm(perm);
					return null;
				}
			con.getForWhom().sendRawMessage(ChatColor.LIGHT_PURPLE+answer+ChatColor.YELLOW+" is not a valid permission");
			return null;
		}
		
		
		if(answer.equalsIgnoreCase("add")){
			Permissions.printPerms(con.getForWhom(), city);
			add();
			return this;
		}
		if(answer.equalsIgnoreCase("remove")){
			Permissions.printPerms(con.getForWhom(), city);
			remove();
			return this;
		}
		return null;
	}

	@Override
	public String getPromptText(ConversationContext con) {
		if(add)
			return ChatColor.YELLOW+"Which of the above would you like to add?";
		
		if(remove)
			return ChatColor.YELLOW+"Which of the above would you like to remove?";
		
		return ChatColor.YELLOW+"Would you like to "+ChatColor.LIGHT_PURPLE+"add"+ChatColor.YELLOW+" or "+ChatColor.LIGHT_PURPLE+"remove"+ChatColor.YELLOW+" permissions from "
				+ChatColor.LIGHT_PURPLE+rank.getName()+ChatColor.YELLOW+"?";
		
	}
	
	private void add(){
		add = true;
	}
	
	private void remove(){
		remove = true;
	}
    	   
}