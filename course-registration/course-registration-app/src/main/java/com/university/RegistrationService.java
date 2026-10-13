
package com.university;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistrationService {

    private static final int MAX_CREDITS = 20;

    // In-memory audit log: one String entry per successful registration.
    private final List<String> auditLog = new ArrayList<>();

    public boolean register(Student student, Course course) {

        if (!course.isActive()) {
            return false;
        }

        // Task 3: reject a course that is already full.
        if (course.getCurrentEnrollment() >= course.getCapacity()) {
            return false;
        }

        if (student.isRegistered(course.getCourseCode())) {
            return false;
        }

        String prerequisite = course.getPrerequisite();

        if (prerequisite != null
                && !prerequisite.isBlank()
                && !student.hasCompletedPrerequisite(prerequisite)) {
            return false;
        }

        if (student.getTotalCredits() + course.getCredits() > MAX_CREDITS) {
            return false;
        }

        // Update registration state only after all validations pass.
        student.addRegistration(
                course.getCourseCode(),
                course.getCredits()
        );

        course.incrementEnrollment();

        // Record the successful registration exactly once.
        String entry = "studentId=" + student.getStudentId()
                + ", courseCode=" + course.getCourseCode();

        auditLog.add(entry);

        return true;
    }

    // Expose a read-only view so callers cannot modify the audit log.
    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }
}
