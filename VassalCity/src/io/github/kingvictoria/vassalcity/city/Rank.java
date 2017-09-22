package io.github.kingvictoria.vassalcity.city;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A city rank
 * 
 * @author KingVitoria
 *
 */
public class Rank implements Serializable {
	
	private static final long serialVersionUID = 803169377515180316L;
	
	private ArrayList<Member> ranked = new ArrayList<Member>();
	private int cityId;
	
	public Rank(City city){
		cityId = city.getId();
	}
	
	public ArrayList<Member> getRanked(){
		return ranked;
	}
	
	public boolean addRanked(Member member){
		if(ranked.contains(member))
			return false;
		
		if(member.getCity().getId() == cityId){
			ranked.add(member);
			return true;
		}
		
		return false;
	}
	
	public boolean removeRanked(Member member){
		if(ranked.contains(member)){
			ranked.remove(member);
			return true;
		}
		
		return false;
	}

}
