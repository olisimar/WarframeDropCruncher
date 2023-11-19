package se.good_omens.relicCruncher;

public enum REFINEMENT {
    INTACT("Intact"),
    EXCEPTIONAL("Exceptional"),
    FLAWLESS("Flawless"),
    RADIANT("Radiant"),
    UNKNOWN("Unknown");


    final String name;
    REFINEMENT(String name) {
        this.name = name;
    }

    public String getRefinementName() {
        return this.name;
    }

    public static REFINEMENT fromString(String inData) {
        for(REFINEMENT rt : REFINEMENT.values()) {
            if(rt.getRefinementName().equalsIgnoreCase(inData.trim())) {
                return rt;
            }
        }
        return UNKNOWN;
    }
}
