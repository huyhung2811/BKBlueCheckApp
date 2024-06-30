package com.example.project3.Model;

public class System {
    private int id;
    private String name;
    private String enrollment_code;

    public System(int id, String name, String enrollment_code) {
        this.id = id;
        this.name = name;
        this.enrollment_code = enrollment_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnrollment_code() {
        return enrollment_code;
    }

    public void setEnrollment_code(String enrollment_code) {
        this.enrollment_code = enrollment_code;
    }
}
