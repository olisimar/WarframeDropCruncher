package se.good_omens.relicCruncher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RCGUI extends Application implements EventHandler<ActionEvent> {

    private static RCSetup rcs;
    Button searchButton;
    ComboBox<String> input;
    //TextField input;
    Spinner<Integer> spinner;
    TabPane resultpane;
    RadioButton allButton;
    RadioButton endlessButton;
    RadioButton oneShotButton;
    ToggleGroup missionTypeGroup;

    public RCGUI() {
    }

    public void SetupRC(RCSetup cruncher) {
        rcs = cruncher;
        if (rcs == null) {
            rcs = new RCSetup();
        }
    }

    public void runLaunch() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Warframe Drop Finder");
        GridPane pane = new GridPane();

        Label label = new Label();
        label.setText("Warframe Drop Finder - Search the prime, where does the parts drops the best?");
        label.setAlignment(Pos.CENTER);
        label.setMinSize(700, 20);
        label.setPrefSize(700, 20);

        input = new ComboBox<String>();
        input.setTooltip(new Tooltip("Enter search text, press Enter for results."));
        input.setEditable(true);
        input.commitValue();
        input.setPrefSize(250, 20);
        input.setOnKeyPressed( event-> {
            if(event.getCode() == KeyCode.ENTER) {
                if(input.getValue() != null) {
                    input.commitValue();
                    if(!input.getItems().contains(input.getValue())) {
                        input.getItems().add(input.getValue());
                    }
                    this.updateResultPane();
                } else {
                    resultpane.getTabs().removeAll();
                    resultpane.getTabs().clear();
                    resultpane.getTabs().add(this.getEmptyTab("Search for nothing and indeed, none shall be found."));
                }
            }
        });

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100, 0);
        spinner = new Spinner<Integer>(valueFactory);
        spinner.setTooltip(new Tooltip("Set the percentage between 0 and 100, anything below the limit will not be shown."));
        spinner.setPrefSize(70,20);

        GridPane.setRowIndex(label, 0);
        GridPane.setColumnSpan(label, 1);
        GridPane.setColumnIndex(label, 0);
        pane.getChildren().add(label);

        GridPane.setRowIndex(input, 0);
        GridPane.setColumnSpan(input, 1);
        GridPane.setColumnIndex(input, 6);
        pane.getChildren().add(input);

        GridPane.setRowIndex(spinner, 0);
        GridPane.setColumnSpan(spinner, 1);
        GridPane.setColumnIndex(spinner, 7);
        pane.getChildren().add(spinner);
        
        
        GridPane missionPane = new GridPane();
        
        Label missionTypeSelection = new Label("Desired mission types:  ");
        missionTypeSelection.setAlignment(Pos.CENTER_RIGHT);
        missionTypeSelection.setMinSize(800, 25);
        missionTypeSelection.setPrefSize(800, 25);
        GridPane.setRowIndex(missionTypeSelection, 0);
        GridPane.setColumnSpan(missionTypeSelection, 1);
        GridPane.setColumnIndex(missionTypeSelection, 0);
        missionPane.getChildren().add(missionTypeSelection);
        
        missionTypeGroup = new ToggleGroup();
        allButton = new RadioButton("All");
        allButton.setSelected(true);
        allButton.setToggleGroup(missionTypeGroup);
        GridPane.setRowIndex(allButton, 0);
        GridPane.setColumnSpan(allButton, 1);
        GridPane.setColumnIndex(allButton, 6);
        missionPane.getChildren().add(allButton);
        endlessButton = new RadioButton("Endless");
        endlessButton.setToggleGroup(missionTypeGroup);
        GridPane.setRowIndex(endlessButton, 0);
        GridPane.setColumnSpan(endlessButton, 1);
        GridPane.setColumnIndex(endlessButton, 7);
        missionPane.getChildren().add(endlessButton);
        oneShotButton = new RadioButton("One Shot");
        oneShotButton.setToggleGroup(missionTypeGroup);
        GridPane.setRowIndex(oneShotButton, 0);
        GridPane.setColumnSpan(oneShotButton, 1);
        GridPane.setColumnIndex(oneShotButton, 8);
        
        missionPane.getChildren().add(oneShotButton);
        GridPane.setRowIndex(missionPane, 1);
        GridPane.setColumnSpan(missionPane, 9);
        GridPane.setColumnIndex(missionPane, 0);
        pane.getChildren().add(missionPane);

        resultpane = new TabPane();
        resultpane.setPrefSize(1078, 750);
        resultpane.setMinSize(1078, 750);
        resultpane.getTabs().add(this.getEmptyTab());
        GridPane.setRowIndex(resultpane, 2);
        GridPane.setColumnSpan(resultpane, 3);
        GridPane.setColumnIndex(resultpane, 0);
        pane.getChildren().add(resultpane);

        Scene mainScene = new Scene(pane, 1078, 800);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        //System.out.println("Show was called...");
    }

    private Tab getEmptyTab() {
        return this.getEmptyTab("");
    }
    private Tab getEmptyTab(String message) {
        return this.getEmptyTab("How to use?", message);
    }
    private Tab getEmptyTab(String title, String message) {
        Tab tab = new Tab(title);
        ScrollPane pane = new ScrollPane();
        Label info = new Label();
        Insets ins = new Insets(20, 20, 20, 20);
        info.setFont(new javafx.scene.text.Font("Verdana", 14));
        StringBuilder sb = new StringBuilder();
        if(message != null && !message.trim().isEmpty()) {
            sb.append(message + System.lineSeparator() +"------------------------------------"+ System.lineSeparator());
        } else {
            sb.append("Welcome!" + System.lineSeparator() + System.lineSeparator());
        }
        sb.append("Use the searchfield in the upper left corner. Once you have entered the prime part" + System.lineSeparator());
        sb.append("you wish to search for - Press Enter. Partial names will suffice which include the"+ System.lineSeparator());
        sb.append("parts name in order to produce a result for it."+ System.lineSeparator());
        sb.append("However if it is not farmable at the moment you will see this text again." + System.lineSeparator());
        sb.append("If another mission reward matches the sought term it will be added as well."+ System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("First item will be the drop percentage, sorted from highest down to the set percentage selected." + System.lineSeparator());
        sb.append("Next is the mission, region and type of mission. This is then followed by the rotation." + System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Also, DE being DE, their droptables changes format now and then. This might get broken "+ System.lineSeparator());
        sb.append("for reasons again. I will update as fast as possible or when asked."+ System.lineSeparator());
        sb.append(System.lineSeparator());

        info.setText(sb.toString());
        info.setPadding(ins);
        pane.setContent(info);
        tab.setContent(pane);
        return tab;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (input.getValue() != null) {
            this.updateResultPane();
        } else {
            resultpane.getTabs().removeAll();
            resultpane.getTabs().clear();
            resultpane.getTabs().add(this.getEmptyTab("Search for nothing and indeed, none shall be found."));
        }
    }

    private void updateResultPane() {
        HashMap<String, ArrayList<MissionReward>> relicDrops;
        HashMap<String, ArrayList<MissionReward>> regularDrops;
        ArrayList<ModDrops> modDrops;

        String soughtTerm = input.getValue().trim();
        Toggle selected = missionTypeGroup.getSelectedToggle();
        
        int setPercentage = spinner.getValue().intValue();

        relicDrops = rcs.getWantedPartRelic(soughtTerm);
        regularDrops = rcs.findRewardInMissions(soughtTerm);
        modDrops = rcs.findModsByName(soughtTerm);

        resultpane.getTabs().removeAll();
        resultpane.getTabs().clear();

        if(soughtTerm.equalsIgnoreCase("illindi") || soughtTerm.equalsIgnoreCase("license")) {
            resultpane.getTabs().add(getEmptyTab("Made with a beer license, if you ever meet me you can buy me a beer."+ System.lineSeparator() +
                            "Aside that, don't be an asshole."+ System.lineSeparator() + System.lineSeparator() +
                            "This piece of code, such as it is, is meant to be free to use and no charging money for it, "+
                            "even if you alter "+ System.lineSeparator() + "it in some way or other. "+
                            "No removing this license, adding to it and so on." + System.lineSeparator() +
                            System.lineSeparator() + "Enjoy, Illindi :)"+ System.lineSeparator()));
        }
        
        if(selected == endlessButton) {
        	relicDrops = this.removeUnwantedMissionTypes(relicDrops, true);
        	regularDrops = this.removeUnwantedMissionTypes(regularDrops, true);
        } else if(selected == oneShotButton) {
        	relicDrops = this.removeUnwantedMissionTypes(relicDrops, false);
        	regularDrops = this.removeUnwantedMissionTypes(regularDrops, false);
        }

        for (String dropName : relicDrops.keySet()) {
            if(!relicDrops.get(dropName).isEmpty()) {
                String relicName = relicDrops.get(dropName).get(0).getName().trim().replace("Relic", "");
                String[] dropNameParts = dropName.split(" ");

                Tab tab = new Tab(relicName + " [" + dropNameParts[0] +" "+ dropNameParts[2] + "]");
                Label relicInfo = new Label();
                relicInfo.setFont(new javafx.scene.text.Font("Verdana", 14));
                StringBuilder sb = new StringBuilder();
                Collections.sort(relicDrops.get(dropName));
                Collections.reverse(relicDrops.get(dropName));
                for (MissionReward reward : relicDrops.get(dropName)) {
                    if (reward.getPercentage() > setPercentage) {
                        if (!reward.getMission().getRegion().toLowerCase().contains("event")) {
                            String percentage = Float.toString(reward.getPercentage());
                            if(reward.getPercentage() < 10.0) {
                                percentage = "  "+ percentage;
                            }
                            String[] parts = percentage.split("\\.");
                            if(parts[1].length() == 1) {
                                percentage = percentage +"0";
                            }
                            sb.append(percentage);
                            if(reward.getRotation() == ROTATION.NONE) {
                                sb.append("% in " + reward.getMission().getMissionName() + " at " + reward.getMission().getRegion() + " (" + reward.getMission().getType().toString() + ")");
                            } else {
                                sb.append("% on "+ reward.getRotation().getRotationName()+ " in " + reward.getMission().getMissionName() + " at " + reward.getMission().getRegion() + " (" + reward.getMission().getType().toString() + ")");
                            }
                            sb.append(System.lineSeparator());
                        }
                    }
                }
                byte[] raw = sb.toString().getBytes();
                String out = new String(raw, StandardCharsets.UTF_8);
                relicInfo.setText(out.toString());
                ScrollPane pane = new ScrollPane();
                pane.setContent(relicInfo);
                tab.setContent(pane);
                if(!sb.toString().trim().isEmpty()) {
                    resultpane.getTabs().add(tab);
                }
            }
        }
        if(resultpane.getTabs().size() == 0) {
            StringBuilder sb = new StringBuilder();
            HashSet<String> relicSet = new HashSet<>();
            for(RelicDrop rd : rcs.getRelicWithSought(soughtTerm)) {
                relicSet.add(rd.getDropName() +" - "+ rd.getRelic().getRelicName());
            }
            ArrayList<String> relicList = new ArrayList<>(relicSet);
            Collections.sort(relicList);
            if(!relicList.isEmpty()) {
                for(String rw : relicList) {
                    sb.append(rw + System.lineSeparator());
                }
                Tab noFarm = getEmptyTab("No Active Relic for '"+ soughtTerm+ "'",
                        "The sought relic '"+ soughtTerm +"' produced no missions to farm " +
                        "for them. Meaning they are out of circulation." + System.lineSeparator() + System.lineSeparator() +
                        "Look into trading these relics."+ System.lineSeparator()  +"------------------------------------"+
                        System.lineSeparator() + sb.toString()
                );
                resultpane.getTabs().add(noFarm);
            }
        }
        
        if(!regularDrops.isEmpty()) {
            for(String sought : regularDrops.keySet()) {
                Tab normalResults = generateTabWithRegularResults(regularDrops.get(sought), sought);
                resultpane.getTabs().add(normalResults);
            }
        }

        if(!modDrops.isEmpty()) {
            for( ModDrops mod : modDrops) {
                Tab modResult = generateModTab(mod);
                resultpane.getTabs().add(modResult);
            }
        }

        if(resultpane.getTabs().size() == 0) {
            Tab noFarm = getEmptyTab("The sought term '"+ soughtTerm +"' produced no missions to farm.");
            resultpane.getTabs().add(noFarm);
        }
    }
    
    private HashMap<String, ArrayList<MissionReward>> removeUnwantedMissionTypes(HashMap<String, ArrayList<MissionReward>> input, boolean keepEndless) {
	    HashMap<String, ArrayList<MissionReward>> tmpRewards = new HashMap<String, ArrayList<MissionReward>>();
		for(Entry<String, ArrayList<MissionReward>> missions : input.entrySet()) {
			ArrayList<MissionReward> tmpMissions = new ArrayList<MissionReward>();
			if(keepEndless) {
				for(MissionReward reward : missions.getValue()) {
					if(reward.getMission().getType().isEndless()) {
						tmpMissions.add(reward);
					}
				}
			} else {
				for(MissionReward reward : missions.getValue()) {
					if(!reward.getMission().getType().isEndless()) {
						tmpMissions.add(reward);
					}
				}
			}
			tmpRewards.put(missions.getKey(), tmpMissions);
		}
		return tmpRewards;
    }

    private Tab generateModTab(ModDrops mod) {
        Tab toReturn;
        if(mod.getSources().size() == 1) {
            toReturn = new Tab("Enemy for '" + mod.getName() + "'");
        } else {
            toReturn = new Tab("Enemies for '" + mod.getName() + "'");
        }
        ArrayList<ModDrops.ModSource> sources = mod.getSources();
        Collections.sort(sources);
        //Collections.reverse(sources);
        StringBuilder sb = new StringBuilder();
        for(ModDrops.ModSource source : sources) {
            if (source.getActualChance() > spinner.getValue().intValue()) {
                String dropPercentage = Float.toString(source.getSourceChance());
                if(source.getSourceChance() < 10.0) {
                    dropPercentage = "  "+ dropPercentage;
                } else if(source.getSourceChance() >= 100.0) {
                } else {
                    dropPercentage = " "+ dropPercentage;
                }
                String[] parts = dropPercentage.split("\\.");
                if(parts[1].length() == 1) {
                    dropPercentage = dropPercentage +"0";
                }
                sb.append(dropPercentage +"% Source Dropchance, ");

                String dropChance = Float.toString(source.getDropChance());
                if(source.getDropChance() < 10.0) {
                    dropChance = " "+ dropChance;
                }
                parts = dropChance.split("\\.");
                if(parts[1].length() == 1) {
                    dropChance = dropChance +"0";
                }
                sb.append(dropChance +"% pool chance ");

                String actualChance = Float.toString(source.getActualChance());
                parts = actualChance.split("\\.");
                if(parts[1].length() == 1) {
                    actualChance = actualChance + "00";
                } else if(parts[1].length() == 2) {
                    actualChance = actualChance +"0";
                } else if(parts[1].length() > 3) {
                    actualChance = parts[0] +"."+ parts[1].substring(0,3);
                }
                sb.append("giving actual odds at "+ actualChance +"% dropping from "+ new EnemyLocations().getEnemyLocation(source.getName()) );
                sb.append(System.lineSeparator());
            }
        }
        ScrollPane pane = new ScrollPane();
        Label info = new Label();
        // Insets ins = new Insets(20, 20, 20, 20);
        info.setFont(new javafx.scene.text.Font("Verdana", 14));
        if(sb.toString().isEmpty()) {
            info.setText("No mission had a drop chance above selected percentage.");
        } else {
            info.setText(sb.toString());
        }
        pane.setContent(info);
        toReturn.setContent(pane);
        return toReturn;
    }

    private Tab generateTabWithRegularResults(ArrayList<MissionReward> rewards, String sought) {
        Tab toReturn;
        if(rewards.size() == 1) {
            toReturn = new Tab("Mission for '"+ sought +"'");
        } else {
            toReturn = new Tab("Missions for '"+ sought +"'");
        }
        Collections.sort(rewards);
        Collections.reverse(rewards);
        StringBuilder sb = new StringBuilder();
        for(MissionReward reward : rewards) {
            if (reward.getPercentage() > spinner.getValue().intValue()) {
                String percentage = Float.toString(reward.getPercentage());
                if(reward.getPercentage() < 10.0) {
                    percentage = "  "+ percentage;
                }
                String[] parts = percentage.split("\\.");
                if(parts[1].length() == 1) {
                    percentage = percentage +"0";
                }
                sb.append(percentage);
                if (reward.getRotation() == ROTATION.NONE) {
                    sb.append("% in " + reward.getMission().getMissionName() + " at " + reward.getMission().getRegion() + " ("+ reward.getMission().getType() +")");
                } else {
                    sb.append("% on " + reward.getRotation().getRotationName() + " in " + reward.getMission().getMissionName() + " at " + reward.getMission().getRegion() + " ("+ reward.getMission().getType().toString() +")");
                }
                sb.append(System.lineSeparator());
            }
        }
        ScrollPane pane = new ScrollPane();
        Label info = new Label();
        // Insets ins = new Insets(20, 20, 20, 20);
        info.setFont(new javafx.scene.text.Font("Verdana", 14));
        if(sb.toString().isEmpty()) {
            info.setText("No mission had a drop chance above selected percentage.");
        } else {
            info.setText(sb.toString());
        }
        pane.setContent(info);
        toReturn.setContent(pane);
        return toReturn;
    }
}
