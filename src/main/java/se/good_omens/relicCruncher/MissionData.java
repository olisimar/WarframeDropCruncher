package se.good_omens.relicCruncher;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Node;

/**
 * Mission relevant data.
 */
public class MissionData {
	
	private String region = "Unknown";
	private String name = "Unknown";
	private MISSIONTYPE type;
	private final ArrayList<MissionReward> rewards = new ArrayList<MissionReward>();
	
	public MissionData(List<Node> nodes) {
		ROTATION rotation = ROTATION.NONE;
		for( Node node : nodes ) {
			if( node.getChildNodes().item(0).getNodeName().equalsIgnoreCase("th") ) {
				ROTATION tmpRotation = ROTATION.fromString(node.getChildNodes().item(0).getTextContent());
				if( tmpRotation != ROTATION.UNKNOWN ) {
					rotation = tmpRotation;
				} else {
					String[] data = node.getTextContent().split("/");
					if(data.length > 1) {
						this.region = data[0].trim();
						String[] rest = data[1].trim().split( "\\(" );
						if( this.region.contains("Duviri")) {
							rest = data[1].trim().split(":");
							String missiontype = rest[1].split("\\(")[0].trim();
							this.type = MISSIONTYPE.fromString( missiontype );
						} else {
							this.type = MISSIONTYPE.fromString(rest[rest.length - 1].replace( ")", "" ).trim() );
						}

						this.name = "";
						for (String part : rest[0].split( " " )) {
							this.name = this.name.concat(" ").concat(part);
						}
						this.name = this.name.trim();
						if(this.name.contains("ÃƒÂ¶")){
							this.name = this.name.replace("ÃƒÂ¶F", "öf");
						} else if(this.name.endsWith("Fler")) {
							this.name = "Stöfler";
						}
					}
				}
			} else if( node.getChildNodes().getLength() == 2 ) {
				MissionReward mr = new MissionReward(node, rotation, this);
				rewards.add( mr );
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
	
	public String getRegion() { return this.region; }
	public String getMissionName() {  return this.name; }
	public List<MissionReward> getRewards() { return this.rewards; }
	public MISSIONTYPE getType() { return this.type; }
	
	@Override
	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append( this.region + "|" + this.name + ":" + this.type );
		for(MissionReward reward : rewards) {
			toReturn.append( "\n> " + reward.toString() );
		}
		return toReturn.toString();
	}
}
