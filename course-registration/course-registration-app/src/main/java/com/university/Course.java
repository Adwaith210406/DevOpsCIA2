package com.university;

public class Course {

    private final String courseCode;
    private final String courseName;
    private final int credits;
    private final int capacity;
    private int currentEnrollment;
    private final boolean active;
    private final String prerequisite;

    public Course(
            String courseCode,
            String courseName,
            int credits,
            int capacity,
            boolean active,
            String prerequisite) {

        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.capacity = capacity;
        this.currentEnrollment = 0;
        this.active = active;
        this.prerequisite = prerequisite;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    public boolean isActive() {
        return active;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void incrementEnrollment() {
        currentEnrollment++;
    }
}