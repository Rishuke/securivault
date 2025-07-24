package com.esgi.securivault.entity;

import java.util.List;
import java.util.Map;

public class Suitcase {
    private String id;
    private String name;
    
    private boolean isLocked;
    
    private boolean isOn;
    
    private String code;
    private List<String> userIds;
    private boolean isMooving;

    private String led_color;
    private int volume;
    private double sensitivity;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setisOn(boolean isOn) {
        this.isOn = isOn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
    public boolean isMooving() {
        return isMooving;
    }
    public void setisMooving(boolean isMooving) {
        this.isMooving = isMooving;
    }
    public String getColor(){
        return led_color;
    }
    public void  setColor(String led_color){
        this.led_color = led_color;
    }
    public int getVolume(){
        return volume;
    }
    public void  setVolume(int volume){
        this.volume = volume;
    }
    public double getMotionSensitivity(){
        return sensitivity;
    }
    public void  setMotionSensitivity(double sensitivity2){
        this.sensitivity = sensitivity2;
    }
}