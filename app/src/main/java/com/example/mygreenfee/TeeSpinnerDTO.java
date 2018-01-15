package com.example.mygreenfee;

/**
 * Created by Julien on 13/01/2018.
 */

public class TeeSpinnerDTO {

    private String id;

    private String name;

    public TeeSpinnerDTO(String i, String s) {
        this.id = i;
        this.name = s;
    }

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

    @Override
    public String toString() {
        return name;
    }
}
