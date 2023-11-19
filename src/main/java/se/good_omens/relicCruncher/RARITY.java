package se.good_omens.relicCruncher;

public enum RARITY {
	VERY_COMMON("Very Common"),
	COMMON("Common"),
	UNCOMMON("Uncommon"),
	RARE("Rare"),
	ULTRA_RARE("Ultra Rare"),
	UNKNOWN("Unknown");
	
	public String rarityName;
	
	RARITY(String rname) {
		this.rarityName = rname;
	}

	public String getRarityName() {
		return this.rarityName;
	}
	
	public static RARITY fromString(String inData) {
		for(RARITY rt : RARITY.values()) {
			if(rt.getRarityName().equalsIgnoreCase(inData.trim())) {
				return rt;
			}
		}
		return UNKNOWN;
	}
}
