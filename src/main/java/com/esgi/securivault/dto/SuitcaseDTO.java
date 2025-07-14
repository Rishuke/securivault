package com.esgi.securivault.dto;



import com.fasterxml.jackson.annotation.JsonProperty;



public class SuitcaseDTO {
    private String id;
    private String name;
    private String code;
    
    private boolean isLocked;
    
    private boolean isOn;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setisLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setisOn(boolean isOn) {
        this.isOn = isOn;
    }
    public boolean isMooving() {
        return isMooving;
    }
    public void setisMooving(boolean isMooving) {
        this.isMooving = isMooving;
    }   
}