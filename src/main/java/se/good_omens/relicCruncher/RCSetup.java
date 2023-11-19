package se.good_omens.relicCruncher;

import org.xml.sax.SAXException;

import javax.sound.sampled.AudioFormat;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RCSetup {
    private final String uri = "https://www.warframe.com/droptables";
    private String fixedFile = "NoFileFound";
    private RelicCruncher rc = null;

    public RCSetup() {
        // System.out.println("Init called");
        WebDrops wd = new WebDrops(uri);
        try {
            String toFile = wd.getDropTableData(uri);
            fixedFile = wd.writeToFile(toFile);
        } catch (IOException e) {
            System.out.println("IO exception, failed. Message: "+ e.getMessage());
            fixedFile = "Warframe_PC_Drops.html";
        }

        // System.out.println("Uri was run, doing setup now.");
        RelicCruncher tempRC = null;
        try {
            tempRC = new RelicCruncher(fixedFile);
            rc = tempRC;
        } catch (SAXException e) {
            System.out.println("Generic: "+ e.getMessage());
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        } catch(FileNotFoundException efn) {
            throw new RuntimeException(">> Failed to parse and backup file. Please ensure that it exist as '"+ fixedFile +"' where you execute the jar. \n");
        } catch (Throwable t) {
            System.out.println("Caught throwable, bad days." + t.getMessage());
            t.printStackTrace();
        }

        if(rc == null) {
            System.out.println("RelicCruncher was null, something went wrong.");
            System.exit(0);
        }
        // System.out.println("Setup done");
    }

    public HashMap<String, ArrayList<MissionReward>> findRewardInMissions(String soughtReward) {
        HashMap<String, ArrayList<MissionReward>> toReturn = new HashMap<String, ArrayList<MissionReward>>();
        ArrayList<MissionReward> rawList = rc.findRewardFromMissions(soughtReward);
        for(MissionReward mr : rawList) {
            if(!mr.getName().toLowerCase().contains("relic")) {
                ArrayList<MissionReward> tmp = toReturn.get(mr.getName());
                if(tmp == null) {
                    tmp = new ArrayList<MissionReward>();
                }
                if(!mr.getMission().getMissionName().trim().isEmpty()) {
                    if(!mr.getMission().getMissionName().toLowerCase().contains("event")) {
                        if(!mr.getMission().getRegion().toLowerCase().contains("event")) {
                            tmp.add(mr);
                            toReturn.put(mr.getName(), tmp);
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    public ArrayList<RelicDrop> getRelicWithSought(String soughtRelic){
        return rc.findRelicsWithRewards(soughtRelic);
    }

    public HashMap<String, ArrayList<MissionReward>> getWantedPartRelic(String wantedPartName) {
        HashMap<String, ArrayList<MissionReward>> toReturn = new HashMap<>();
        ArrayList<RelicDrop> relicDrops = rc.findRelicsWithRewards(wantedPartName);
        ArrayList<String> relicNames = new ArrayList<>();

        for( RelicDrop drop : relicDrops ) {
            if(!relicNames.contains(drop.getRelic().getRelicName())) {
                relicNames.add(drop.getRelic().getRelicName());
                //System.out.println("Added: "+ drop.getRelic().getRelicName() +" as sought has "+ drop.getDropName());
                ArrayList<MissionReward> rewardsFromRelicName = rc.findRewardFromMissions(drop.getRelic().getRelicName());
                if(!rewardsFromRelicName.isEmpty()) {
                    toReturn.put(drop.getDropName(), rewardsFromRelicName);
                }
            }
        }
        return toReturn;
    }

    private void printRewards(ArrayList<MissionReward> rewards, int abovePercentage) {
        if(rewards.size() != 0) {
            rewards.sort(null);
            for(MissionReward reward : rewards) {
                if( reward.getPercentage() > abovePercentage ) {
                    if( !reward.getMission().getRegion().contains("Event") ) {
                        System.out.println("["+ reward.getMission().getRegion() +":"+ reward.getMission().getMissionName() +":"+ reward.getMission().getType().toString() +"] "+ reward.toString());
                    }
                }
            }
        } else {
            System.out.println("No results");
        }
    }

    public void print(HashMap<String, ArrayList<MissionReward>> crap) {
        for(String relicitem : crap.keySet()) {
            System.out.println("Relic: "+ relicitem);
            for (MissionReward reward : crap.get(relicitem)) {
                System.out.println("["+ reward.getMission().getRegion() +":"+ reward.getMission().getMissionName() +":"+ reward.getMission().getType().toString() +"] "+ reward.toString());
            }
        }
    }

    public ArrayList<ModDrops> findModsByName(String soughtTerm) {
        return rc.findModdropsByName(soughtTerm);
    }
}
