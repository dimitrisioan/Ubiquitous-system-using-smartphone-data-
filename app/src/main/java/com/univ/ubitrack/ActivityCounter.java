package com.univ.ubitrack;

public class ActivityCounter {

    private String activity_type;
    private int count;

    public ActivityCounter(String activity_type, int count) {
        this.activity_type = activity_type;
        this.count = count;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
