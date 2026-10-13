package com.university;

import java.util.HashSet;
import java.util.Set;

public class Student {

    private final String studentId;
    private final String studentName;
    private final Set<String> completedPrerequisites;
    private final Set<String> registeredCourses;
    private int totalCredits;

    public Student(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.completedPrerequisites = new HashSet<>();
        this.registeredCourses = new HashSet<>();
        this.totalCredits = 0;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Set<String> getCompletedPrerequisites() {
        return completedPrerequisites;
    }

    public Set<String> getRegisteredCourses() {
        return registeredCourses;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void addCompletedPrerequisite(String courseCode) {
        completedPrerequisites.add(courseCode);
    }

    public boolean hasCompletedPrerequisite(String courseCode) {
        return completedPrerequisites.contains(courseCode);
    }

    public boolean isRegistered(String courseCode) {
        return registeredCourses.contains(courseCode);
    }

    public void addRegistration(String courseCode, int credits) {
        registeredCourses.add(courseCode);
        totalCredits += credits;
    }
}