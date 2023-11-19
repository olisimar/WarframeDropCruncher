package se.good_omens.relicCruncher;

public enum ROTATION {

	NONE("None"),
	ROTATION_A("Rotation A"),
	ROTATION_B("Rotation B"),
	ROTATION_C("Rotation C"),
	UNKNOWN("Unknown");
	
	public String rotationName;
	
	ROTATION(String name) {
		this.rotationName = name;
	}
	
	public String getRotationName() {
		return this.rotationName;
	}
	
	public static ROTATION fromString(String inData) {
		for( ROTATION rotation : ROTATION.values() ) {
			if( rotation.getRotationName().equalsIgnoreCase(inData) ) {
				return rotation;
			}
		}
		return UNKNOWN;
	}

}
