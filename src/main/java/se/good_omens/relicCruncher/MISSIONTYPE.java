package se.good_omens.relicCruncher;

public enum MISSIONTYPE {
	
	ARENA("Arena", false),
	ASSASSINATION("Assassination", false),
	CACHES("Caches", false),
	CAPTURE("Capture", false),
	CONCLAVE("Conclave", false),
	DEFECTION("Defection", true),
	DEFENSE("Defense", true),
	DISRUPTION("Disruption", true),
	EXCAVATION("Excavation", true),
	EXTERMINATE("Exterminate", false),
	INFESTED_SALVAGE("Infested Salvage", true),
	INTERCEPTION("Interception", true),
	MOBILE_DEFENSE("Mobile Defense", false),
	PURSUIT("Pursuit", false),
	REPATED_REWARDS("Repeated Rewards", true),
	RESCUE("Rescue", false),
	RUSH("Rush", false),
	SABOTAGE("Sabotage", false),
	SPY("Spy", false),
	SURVIVAL("Survival", true),
	SANCTUARY_ONSLAUGHT("Sanctuary Onslaught", true),
	SKIRMISH("Skirmish", false),
	VOID_ARMAGEDDON("Void Armageddon", true),
	VOID_CASCADE("Void Cascade", true),
	VOID_FLOOD("Void Flood", true),
	DUVIRI_ENDLESS_1("Tier 1", true),
	DUVIRI_ENDLESS_2("Tier 2", true),
	DUVIRI_ENDLESS_3("Tier 3", true),
	DUVIRI_ENDLESS_4("Tier 4", true),
	DUVIRI_ENDLESS_5("Tier 5", true),
	DUVIRI_ENDLESS_6("Tier 6", true),
	DUVIRI_ENDLESS_7("Tier 7", true),
	DUVIRI_ENDLESS_8("Tier 8", true),
	DUVIRI_ENDLESS_9("Tier 9", true),
	MIRROR_DEFENSE("Mirror Defense", true),
	ALCHEMY("Alchemy", true),
	ASCENSION("Ascension", false),
	SHRINE_DEFENSE("Shrine Defense", false),
	LEGACYTE_HARVEST("Legacyte Harvest", true),
	UNKNOWN("Unknown", false);

	final String name;
	final boolean endless;
	MISSIONTYPE(String name, boolean endless) {
		this.name = name;
		this.endless = endless;
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
	
	public boolean isEndless() {
		return this.endless;
	}
}
