package com.univ.ubitrack;

public class DeviceModel {

    private int id;
    private int recruitedTeam;
    private String ageRange;
    private String gender;
    private String device_id;
    private int isDeviseRegistered;

    public DeviceModel(int id, int recruitedTeam, String ageRange, String gender, String device_id, int isDeviseRegistered) {
        this.id = id;
        this.recruitedTeam = recruitedTeam;
        this.ageRange = ageRange;
        this.gender = gender;
        this.device_id = device_id;
        this.isDeviseRegistered = isDeviseRegistered;
    }

    public DeviceModel(){

    }

    @Override
    public String toString() {
        return "DeviceModel{" +
                "id=" + id +
                ", recruitedTeam=" + recruitedTeam +
                ", ageRange='" + ageRange + '\'' +
                ", gender='" + gender + '\'' +
                ", device_id='" + device_id + '\'' +
                ", isDeviseRegistered='" + isDeviseRegistered + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecruitedTeam() {
        return recruitedTeam;
    }

    public void setRecruitedTeam(int recruitedTeam) {
        this.recruitedTeam = recruitedTeam;
    }

    public void setIsDeviseRegistered(int isDeviseRegistered) {
        this.isDeviseRegistered = isDeviseRegistered;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getIsDeviseRegistered() {
        return isDeviseRegistered;
    }
}
