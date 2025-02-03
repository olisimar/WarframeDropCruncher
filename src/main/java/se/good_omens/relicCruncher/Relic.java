package se.good_omens.relicCruncher;

import java.util.LinkedList;
import java.util.ArrayList;

import org.w3c.dom.Node;

public class Relic {

	private String relicName = "NoName";
	private ArrayList<RelicDrop> relicDrops = new ArrayList<RelicDrop>();
	private RARITY rarity = RARITY.UNKNOWN;
	private REFINEMENT refinement;

	public Relic(LinkedList<Node> nodes) {
		for( Node node : nodes ) {
			if( node.getChildNodes().item(0).getNodeName().equalsIgnoreCase("th") ) {
				String[] data = node.getTextContent().split("\\(");
				this.relicName = data[0].trim();
				String refinement = data[1].replace(")", "");
				this.refinement = REFINEMENT.fromString(refinement);
			} else if( node.getChildNodes().getLength() == 2 ) {
				RelicDrop rd = new RelicDrop(node, this);
				relicDrops.add( rd );
			} else {
				// WTF section.
				try {
					System.out.println( node.getChildNodes().getLength() + " : " + node.getChildNodes().item(0).getLastChild().getTextContent() );
				} catch( Exception e ) {
					System.out.println( "Node was empty, this should not happen." );
				}
			}
		}
	}

	public ArrayList<RelicDrop> getRelicDrops() {
		return this.relicDrops;
	}

	public String getRelicName() {
		return this.relicName;
	}
	
	public REFINEMENT getRefinement() {
		return this.refinement;
	}
	
	public RARITY getRarity() {
		return this.rarity;
	}
}
