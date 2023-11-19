package se.good_omens.relicCruncher;

public class RCRunner {

	public static void main(String[] args) {
		RCSetup rcs = new RCSetup();
		RCGUI gui = new RCGUI();
		gui.SetupRC(rcs);
		gui.runLaunch();
	}
}
