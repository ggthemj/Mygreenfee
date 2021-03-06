package com.legreenfee;

import java.util.List;

/**
 * Created by Julien on 13/01/2018.
 */

public class TeeSpinnerDTO {

    private List<String> id;

    private String name;

    private String courseId;

    public TeeSpinnerDTO(int courseId, List<String> teeId, String message) {
        this.id = teeId;
        this.name = message;
        this.courseId = String.valueOf(courseId);
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return name;
    }
}
