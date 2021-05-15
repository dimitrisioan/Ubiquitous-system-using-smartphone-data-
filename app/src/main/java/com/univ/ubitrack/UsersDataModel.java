package com.univ.ubitrack;

public class UsersDataModel {

    private int id;
    private String device_interactive;
    private int display_state;
    private String system_time;
    private String activity;
    private float activity_conf;
    private String location_type;
    private String location_id;
    private float location_conf;
    private int battery_level;
    private String battery_status;
    private String network_type;
    private int notifs_active;
    private int added_thinksboard;

    public UsersDataModel(int id, String device_interactive, int display_state, String system_time,
                          String activity, float activity_conf, String location_type, String location_id,
                          float location_conf, int battery_level, String battery_status, String network_type,
                          int notifs_active, int added_thinksboard) {
        this.id = id;
        this.device_interactive = device_interactive;
        this.display_state = display_state;
        this.system_time = system_time;
        this.activity = activity;
        this.activity_conf = activity_conf;
        this.location_type = location_type;
        this.location_id = location_id;
        this.location_conf = location_conf;
        this.battery_level = battery_level;
        this.battery_status = battery_status;
        this.network_type = network_type;
        this.notifs_active = notifs_active;
        this.added_thinksboard = added_thinksboard;
    }

    public String getDevice_interactive() {
        return device_interactive;
    }

    public void setDevice_interactive(String device_interactive) {
        this.device_interactive = device_interactive;
    }

    public int getAdded_thinksboard() {
        return added_thinksboard;
    }

    public void setAdded_thinksboard(int added_thinksboard) {
        this.added_thinksboard = added_thinksboard;
    }

    public int getId() {
        return id;
    }

    public int getDisplay_state() {
        return display_state;
    }

    public void setDisplay_state(int display_state) {
        this.display_state = display_state;
    }

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public float getActivity_conf() {
        return activity_conf;
    }

    public void setActivity_conf(float activity_conf) {
        this.activity_conf = activity_conf;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public float getLocation_conf() {
        return location_conf;
    }

    public void setLocation_conf(float location_conf) {
        this.location_conf = location_conf;
    }

    public int getBattery_level() {
        return battery_level;
    }

    public void setBattery_level(int battery_level) {
        this.battery_level = battery_level;
    }

    public String getBattery_status() {
        return battery_status;
    }

    public void setBattery_status(String battery_status) {
        this.battery_status = battery_status;
    }

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public int getNotifs_active() {
        return notifs_active;
    }

    public void setNotifs_active(int notifs_active) {
        this.notifs_active = notifs_active;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UsersDataModel{" +
                "id=" + id +
                ", device_interactive='" + device_interactive + '\'' +
                ", display_state=" + display_state +
                ", system_time='" + system_time + '\'' +
                ", activity='" + activity + '\'' +
                ", activity_conf=" + activity_conf +
                ", location_type='" + location_type + '\'' +
                ", location_id='" + location_id + '\'' +
                ", location_conf=" + location_conf +
                ", battery_level=" + battery_level +
                ", battery_status='" + battery_status + '\'' +
                ", network_type='" + network_type + '\'' +
                ", notifs_active=" + notifs_active +
                '}';
    }
}