package com.recommendations;

import java.util.Comparator;
import java.util.Map;

class ValueComparator implements Comparator<Integer> {

    Map<Integer, Float> base;
    public ValueComparator(Map<Integer, Float> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
