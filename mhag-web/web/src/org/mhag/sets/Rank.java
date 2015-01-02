package org.mhag.sets;

/**
 * Created by bronwen.
 * Date: 22/02/12
 * Time: 10:12
 */
public class Rank {

    public Rank(int index, String label) {
        this.index = index;
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private int index;
    private String label;
}
