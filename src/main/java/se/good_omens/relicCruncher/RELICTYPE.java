package se.good_omens.relicCruncher;

public enum RELICTYPE {
	
	LITH,
	MESO,
	NEO,
	AXI,
	
	UNKNOWN;
	
	public RELICTYPE fromString(String inData) {
		for(RELICTYPE rt : RELICTYPE.values()) {
			if(rt.toString().equalsIgnoreCase(inData)) {
				return rt;
			}
		}
		return UNKNOWN;
	}
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
