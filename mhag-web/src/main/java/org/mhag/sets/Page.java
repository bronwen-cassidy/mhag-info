package org.mhag.sets;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by bronwen.
 * Date: 15/02/12
 * Time: 18:19
 */
public class Page<T> implements Serializable {

    public static final int MAX_RESULTS = 50;

    public Set<T> getData() {
        return data;
    }

    public void setData(Set<T> data) {
        this.data = data;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return getNumPages() * 50;
    }
    
    public int getNumPages() {
        return totalResults / MAX_RESULTS;
    }
    
    public int getPrev() {
        return getFirst() - MAX_RESULTS;
    }
    
    public int getNext() {
        return getFirst() + MAX_RESULTS;
    }

    private int totalResults;
    private int first;
    private Set<T> data;
}

