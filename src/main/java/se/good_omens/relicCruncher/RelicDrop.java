package se.good_omens.relicCruncher;

import org.w3c.dom.Node;

public class RelicDrop implements Comparable<RelicDrop> {
    private String dropName = "Unknown";
    private RARITY rarity = RARITY.UNKNOWN;
    private float percentage;
    private Relic relic;

    public RelicDrop(String relicName, RARITY rarity, float percentage ) {
        this.dropName = this.dropName;
        this.rarity =  rarity;
        this.percentage = percentage;
    }

    public RelicDrop(Node node, Relic relic) {
        this.relic = relic;

        String name = node.getFirstChild().getTextContent();
        String odds = node.getLastChild().getTextContent();
        this.dropName = name;

        String[] ding = odds.split( "\\(" );
        this.rarity = RARITY.fromString(ding[0].trim());
        this.percentage = Float.parseFloat( ding[1].replace( ")", "" ).replace ("%", "" ).trim() );
    }

    public String getDropName() {
        return this.dropName;
    }
    public RARITY getRarity() {
        return this.rarity;
    }
    public float getPercentage() {
        return this.percentage;
    }
    public Relic getRelic() { return this.relic; }

    @Override
    public int compareTo(RelicDrop o) {
        return String.CASE_INSENSITIVE_ORDER.compare(o.getDropName(), this.getDropName());
    }
}
