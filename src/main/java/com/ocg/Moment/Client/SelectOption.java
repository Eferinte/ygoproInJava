package com.ocg.Moment.Client;

import java.util.ArrayList;

public class SelectOption {
    public String label;
    public int value;
    public boolean available = true;

    public SelectOption(String label, int value, boolean available) {
        this.label = label;
        this.value = value;
        this.available = available;
    }
    public SelectOption(String label, int value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String toString() {
        return label;
    }

    public static <T> ArrayList<SelectOption>getOptions(ArrayList<T> arrayList){
        ArrayList<SelectOption> opts = new ArrayList<>();
        for(int z=0;z<arrayList.size();z++){
            opts.add(new SelectOption(String.valueOf(arrayList.get(z)),z));
        }
        return  opts;
    }
}
