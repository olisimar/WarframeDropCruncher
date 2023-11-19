package se.good_omens.relicCruncher;

public enum MISSIONTYPE {
	
	ARENA("Arena"),
	ASSASSINATION("Assassination"),
	CACHES("Caches"),
	CAPTURE("Capture"),
	CONCLAVE("Conclave"),
	DEFECTION("Defection"),
	DEFENSE("Defense"),
	DISRUPTION("Disruption"),
	EXCAVATION("Excavation"),
	EXTERMINATE("Exterminate"),
	INFESTED_SALVAGE("Infested Salvage"),
	INTERCEPTION("Interception"),
	MOBILE_DEFENSE("Mobile Defense"),
	PURSUIT("Pursuit"),
	REPATED_REWARDS("Repeated Rewards"),
	RESCUE("Rescue"),
	RUSH("Rush"),
	SABOTAGE("Sabotage"),
	SPY("Spy"),
	SURVIVAL("Survival"),
	SANCTUARY_ONSLAUGHT("Sanctuary Onslaught"),
	SKIRMISH("Skirmish"),
	VOID_ARMAGEDDON("Void Armageddon"),
	VOID_CASCADE("Void Cascade"),
	VOID_FLOOD("Void Flood"),
	DUVIRI_ENDLESS_1("Tier 1"),
	DUVIRI_ENDLESS_2("Tier 2"),
	DUVIRI_ENDLESS_3("Tier 3"),
	DUVIRI_ENDLESS_4("Tier 4"),
	DUVIRI_ENDLESS_5("Tier 5"),
	DUVIRI_ENDLESS_6("Tier 6"),
	DUVIRI_ENDLESS_7("Tier 7"),
	DUVIRI_ENDLESS_8("Tier 8"),
	DUVIRI_ENDLESS_9("Tier 9"),
	UNKNOWN("Unknown");

	String name;
	MISSIONTYPE(String name) {
		this.name = name;
	}

	public static MISSIONTYPE fromString(String inData) {
		if(inData.contains("Extra")) {
			inData = inData.replace("Extra", "").trim();
		}
		for( MISSIONTYPE md : MISSIONTYPE.values() ) {
			if( md.toString().equalsIgnoreCase(inData.trim()) ) {
				return md;
			}
		}
		System.out.println("Unknown mission type found: "+ inData);
		return UNKNOWN;
	}
	
	public String toString() {
		return this.name;
	}
}
