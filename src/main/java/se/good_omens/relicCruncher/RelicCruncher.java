package se.good_omens.relicCruncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class RelicCruncher {
	private final String fileName;
	private final ArrayList<MissionData> missionData;
	private final ArrayList<Relic> relicData;
	private final ArrayList<ModDrops> modData;

	public RelicCruncher(String fileName) throws SAXException, FileNotFoundException {
		this.fileName = fixFileContent( fileName );
		this.missionData = parseOutMissions(this.fileName);
		this.relicData = parseOutRelics(this.fileName);
		this.modData = parseOutModsbyDrop(this.fileName);
	}

	public ArrayList<MissionReward> findRewardFromMissions(String sought) {
		ArrayList<MissionReward> rewards =  new ArrayList<MissionReward>();
		for(MissionData mission : this.missionData) {
			for(MissionReward reward : mission.getRewards()) {
				if(reward.getName().toLowerCase().contains(sought.toLowerCase())) {
					rewards.add(reward);
				}
			}
		}
		return rewards;
	}

	public ArrayList<RelicDrop> findRelicsWithRewards(String sought) {
		ArrayList<RelicDrop> toReturn = new ArrayList<>();
		for(Relic rdata : this.relicData) {
			for(RelicDrop drop : rdata.getRelicDrops() ) {
				if(drop.getDropName().toLowerCase().contains(sought.toLowerCase())) {
					toReturn.add(drop);
				}
			}
		}
		return toReturn;
	}

	public ArrayList<ModDrops> findModdropsByName(String sought) {
		ArrayList<ModDrops> toReturn = new ArrayList<>();
		for(ModDrops mod : this.modData) {
			if(mod.getName().toLowerCase().contains(sought.toLowerCase())) {
				toReturn.add(mod);
			}
		}
		return toReturn;
	}

	public @NotNull ArrayList<MissionData> parseOutMissions(String fileName ) throws SAXException {
		ArrayList<MissionData> toReturn = new ArrayList<MissionData>();
		try {
			Node dummy = parseIndata(fileName, 0);
			LinkedList<Node> missionItems = new LinkedList<Node>();
			if(dummy != null) {
				for (int i = 0; i < dummy.getChildNodes().getLength(); i++) {
					Node missionData = dummy.getChildNodes().item(i);

					if (missionData.getChildNodes().getLength() == 0 || missionData.getChildNodes().item(0) == null) {
						if (missionItems.size() != 0) {
							MissionData missiond = new MissionData(missionItems);
							toReturn.add(missiond);
							missionItems = new LinkedList<Node>();
						}
					} else if (missionData.getChildNodes().item(0).getNodeName().equalsIgnoreCase("th") || missionData.getChildNodes().getLength() == 2) {
						missionItems.add(missionData);
					}
				}
			}
		} catch(IOException | ParserConfigurationException | NullPointerException e ) {
			e.printStackTrace();
		}

		return toReturn;
	}


	public ArrayList<ModDrops> parseOutModsbyDrop( String fileName ) throws SAXException, FileNotFoundException {
		ArrayList<ModDrops> toReturn = new ArrayList<ModDrops>();
		try {
			Node dummy = parseIndata(fileName, 10);
			if(dummy != null) {
				ModDrops drop = null;
				for (int i = 0; i < dummy.getChildNodes().getLength(); i++) {
					Node modData = dummy.getChildNodes().item(i);
					if (modData.getChildNodes().getLength() == 1) {
						if (modData.getChildNodes().item(0).getNodeName().equalsIgnoreCase("th")) {
							drop = new ModDrops(modData.getTextContent());
						} else if(modData.getChildNodes().item(0).getNodeName().equalsIgnoreCase("td")) {
							toReturn.add(drop);
						}
					} else if(modData.getChildNodes().getLength() == 3) {
						if (modData.getChildNodes().item(0).getNodeName().equalsIgnoreCase("td") && modData != null) {
							drop.addDropper(modData);
						}
					}
				}
			}
		} catch(IOException | ParserConfigurationException | NullPointerException e ) {
			e.printStackTrace();
		}
		return toReturn;
	}

	ArrayList<Relic> parseOutRelics( String fileName ) {
		ArrayList<Relic> relics = new ArrayList<Relic>();

		try {
			Node dummy = parseIndata( fileName, 1 );
			LinkedList<Node> relicItems = new LinkedList<Node>();

			if(dummy != null) {
				for(int i = 0; i < dummy.getChildNodes().getLength(); i++) {
					Node relicData = dummy.getChildNodes().item(i);

					if( relicData.getChildNodes().getLength() == 0 || relicData.getChildNodes().item(0) == null ) {
						if( relicItems.size() != 0 ) {
							Relic relicd = new Relic( relicItems );
							relics.add(relicd);
							relicItems = new LinkedList<Node>();
						}
					} else if( relicData.getChildNodes().item(0).getNodeName().equalsIgnoreCase("th") ||  relicData.getChildNodes().getLength() == 2 ) {
						relicItems.add( relicData );
					}
				}
			}
		} catch(SAXException | IOException | ParserConfigurationException | NullPointerException e ) {
			e.printStackTrace();
		}
		return relics;
	}

	public String fixFileContent( String fileName ) throws FileNotFoundException {
		StringBuilder toReturn = new StringBuilder();
		File myObj = new File( fileName );

		boolean saveData = false;
		boolean ignoreLines = false;
		int ulDepth = 0;
		
		try {
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				data = data.replaceAll("& ", "&amp; ");
				data = data.replaceAll("<p>", "");
				data = data.replaceAll("</p>", "");

				if (data.contains("<br>")) {
					saveData = true;
					data = data.replace("<br>", "<br/>");

				} else if (data.contains("<meta")) {
					String[] parts = data.split("<meta");
					String saveThis = "";
					if (parts.length > 2) {
						for (String part : parts) {
							if (part.startsWith("<")) {
								saveThis = saveThis + part;
							}
						}
					} else {
						data = parts[0] + "<meta charset=\"UTF-8\" />";
					}
					saveData = true;
				} else if(data.contains("<ul>") ) {
					ignoreLines = true;
					ulDepth++;
				} else if (data.contains("<style>")) {
					ignoreLines = true;
					String[] parts = data.split("<style>");
					if (data.length() == 7) {
						// one line with style;
					} else if (parts.length > 2) {
						if (parts[1].contains("</style>")) {
							String[] items = parts[2].split("</style>");
							if (items.length > 2) {
								data = data + parts[1];
							}
//							ignoreLines = true;
						}
					} else {
//						ignoreLines = true;
					}
					saveData = true;
				} else if (data.contains("</style>") || data.contains("</ul>")) {
					if(data.contains("</ul>")) {
						ulDepth--;
						if (ulDepth == 0) {
							ignoreLines = false;
							data = "";
						}
					} else if(data.contains("</style>")) {
						ignoreLines = false;
						data = "";
					}
				}

				if (!ignoreLines) {
					toReturn.append(data).append(System.lineSeparator());
				}
			}

			if (saveData) {
				String[] fileNameParts = fileName.split("\\.");
				String fileNameFixed = fileNameParts[0] + "_fixed.html";
				Path path = Paths.get(fileNameFixed);
				Files.writeString(path, toReturn.toString().trim(), StandardCharsets.UTF_8);
				fileName = fileNameFixed;
			}

			myReader.close();
			return fileName;
		} catch( FileNotFoundException fnfe ) {
			throw new FileNotFoundException("No file found, tried to fix "+ fileName);
		} catch(IOException ioe) {
			System.out.println("Fixing File Content failed. Tried to fix: "+ fileName);
			System.out.println(ioe.getMessage());
		}

		return fileName;
	}

	public Node parseIndata(String fileName, int node) throws SAXException, IOException, ParserConfigurationException, FileNotFoundException {
		DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder DB = DBF.newDocumentBuilder();
		Document DOC = DB.parse(new File( fileName ));
		DOC.getDocumentElement().normalize();
		NodeList nlist = DOC.getElementsByTagName("table");
		return nlist.item(node);
	}
}
