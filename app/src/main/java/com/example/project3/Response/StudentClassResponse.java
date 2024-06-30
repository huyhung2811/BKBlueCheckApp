package com.example.project3.Response;

import com.google.gson.annotations.SerializedName;

public class StudentClassResponse {
    @SerializedName("class_id")
    private int class_id;
    @SerializedName("class_name")
    private String class_name;
    @SerializedName("unit")
    private String unit;
    @SerializedName("system")
    private String system;

    public StudentClassResponse(int class_id,String class_name, String unit, String system) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.unit = unit;
        this.system = system;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
