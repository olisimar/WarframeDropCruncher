package se.good_omens.relicCruncher;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.w3c.dom.Node;

public class ModDrops {
    private String name;
    private ArrayList<ModSource> modSources = new ArrayList<>();

    public ModDrops(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
    public ArrayList<ModSource> getSources() { return this.modSources; }

    public void addDropper(Node node) {
        String dropperName = node.getChildNodes().item(0).getTextContent();
        float sourceChance = Float.parseFloat(node.getChildNodes().item(1).getTextContent().replaceAll("%", ""));
        String tmp = node.getChildNodes().item(2).getTextContent().replaceAll("%", "").trim();
        String[] data = tmp.split("\\(");
        float dropChance = 0;
        try {
            dropChance = Float.parseFloat(data[1].replaceAll("\\)", ""));
        } catch (Throwable t) {
            dropChance = -1.0F;
        }

        ModSource source = new ModSource(dropperName, sourceChance, dropChance);
        modSources.add(source);
    }

    @Override
    public String toString() {
        String toReturn = this.name +" drops from "+ System.lineSeparator();
        for(ModSource source : this.modSources) {
            toReturn = toReturn + source + System.lineSeparator();
        }
        return toReturn;
    }


    class ModSource implements Comparable<ModSource> {
        private String name;
        private float sourceChance = 0;
        private float dropChance = 0;

        public String getName() { return this.name; }
        public float getSourceChance() { return this.sourceChance; }
        public float getDropChance() { return this.dropChance; }
        public Float getActualChance() { return new Float( ((this.dropChance /100) * (this.sourceChance /100 )) * 100); }

        public ModSource(String name, float source, float drop) {
            this.name =  name;
            this.sourceChance = source;
            this.dropChance = drop;
        }

        @Override
        public int compareTo(ModSource o) {
            return o.getActualChance().compareTo(this.getActualChance());
        }

        @Override
        public String toString() {
            return "Drops with actual chance at "+ this.getActualChance() +"% from "+ this.name +".";
        }
    }
}
