package com.esgi.securivault.entity;

import java.util.Map;

public class Suitcase {
    private String id;
    private String name;
    
    private boolean isLocked;
    
    private boolean isOn;
    
    private String code;
    private Map<String, Boolean> userIds;
    private boolean isMooving;


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

    public Map<String, Boolean> getUserIds() {
        return userIds;
    }

    public void setUserIds(Map<String, Boolean> userIds) {
        this.userIds = userIds;
    }
    public boolean isMooving() {
        return isMooving;
    }
    public void setisMooving(boolean isMooving) {
        this.isMooving = isMooving;
    }
}