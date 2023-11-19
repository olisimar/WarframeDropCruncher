package se.good_omens.relicCruncher;

import org.w3c.dom.Node;

public class MissionReward implements Comparable<MissionReward> {
	
	private final String name;
	private final RARITY rarity;
	private final float percentage;
	private final ROTATION rotation;
	private final MissionData mission;
	
	public MissionReward(Node missionReward, ROTATION rotation, MissionData mission) {
		this.rotation = rotation; 	
		this.mission = mission;
		
		String reward = missionReward.getFirstChild().getTextContent();
		String odds = missionReward.getLastChild().getTextContent();
		this.name = reward;
		
		String[] ding = odds.split( "\\(" );
		this.rarity = RARITY.fromString(ding[0].trim());
		this.percentage = Float.parseFloat( ding[1].replace( ")", "" ).replace ("%", "" ).trim() );
	}
	
	public String getName() {
		return this.name;
	}
	public RARITY getRarity() {
		return this.rarity;
	}
	public ROTATION getRotation() {
		return this.rotation;
	}
	public Float getPercentage() {
		return this.percentage;
	}
	public MissionData getMission() {
		return this.mission;
	}

	@Override
	public String toString() {
		return "[" + this.rotation + "] " + this.name + " - " + this.percentage +" "+ this.rarity;
	}

	@Override
	public int compareTo(MissionReward o) {
		if( this.percentage == o.getPercentage() ) {
			return Integer.compare(this.rotation.ordinal(), o.getRotation().ordinal());
//			if( this.rotation.ordinal() > o.getRotation().ordinal() ) {
//				return 1;
//			} else if( this.rotation.ordinal() < o.getRotation().ordinal() ) {
//				return -1;
//			} else {
//				return 0;
//			}
		} else {
			return Float.compare(this.percentage, o.getPercentage());
		}
	}
}
