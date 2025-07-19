package com.esgi.securivault.dto;



import com.fasterxml.jackson.annotation.JsonProperty;



public class SuitcaseDTO {
    private String id;
    private String name;
    private String code;
    private String color;
    private String sensitivity;
    private String buzzer_freq;
    
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
    public String getColor(){
        return color;
    }
    public void  setColor(String color){
        this.color = color;
    }
    public String getbuzzer_freq(){
        return buzzer_freq;
    }
    public void setbuzzer_freq(String buzzer_freq){
        this.buzzer_freq = buzzer_freq;
    }
    public String getMotionSensitivity(){
        return sensitivity;
    }
    public void  setMotionSensitivity(String sensitivity){
        this.sensitivity = sensitivity;
    }
}