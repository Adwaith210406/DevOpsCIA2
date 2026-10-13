package com.university;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                false,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectMissingPrerequisite() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {

        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
    }

    @Test
    void shouldRejectDuplicateRegistration() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        boolean secondAttempt = service.register(student, course);

        assertFalse(secondAttempt);
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCreditLimitIsExceeded() {

        Student student = new Student("S101", "Anita");

        // Simulate 20 existing credits
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectRegistrationWhenCourseIsFull() {
        Student student = new Student("S102", "Rahul");

        Course course = new Course(
                "CS302",
                "Operating Systems",
                4,
                2,
                true,
                null
        );

        // Simulate a course that has reached its capacity.
        course.incrementEnrollment();
        course.incrementEnrollment();

        RegistrationService service = new RegistrationService();

        int initialEnrollment = course.getCurrentEnrollment();
        int initialCredits = student.getTotalCredits();
        int initialRegistrations = student.getRegisteredCourses().size();

        boolean result = service.register(student, course);

        assertFalse(result, "A full course must reject registration");
        assertEquals(initialEnrollment, course.getCurrentEnrollment());
        assertEquals(initialCredits, student.getTotalCredits());
        assertEquals(initialRegistrations,
                student.getRegisteredCourses().size());
        assertFalse(student.isRegistered("CS302"));
    }

    @Test
    void shouldAllowRegistrationWhenExactlyOneSeatRemains() {
        Student student = new Student("S103", "Priya");

        Course course = new Course(
                "CS303",
                "Computer Networks",
                3,
                2,
                true,
                null
        );

        // One of the two available seats is already occupied.
        course.incrementEnrollment();

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result, "The last available seat should be accepted");
        assertEquals(2, course.getCurrentEnrollment());
        assertTrue(student.isRegistered("CS303"));
        assertEquals(3, student.getTotalCredits());
    }

    @Test
    void shouldCreateOneAuditEntryForSuccessfulRegistration() {
        Student student = new Student("S201", "Anita");
        Course course = new Course(
                "CS501", "Database Systems", 4, 30, true, null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        assertEquals(1, service.getAuditLog().size());
        assertEquals(
                "studentId=S201, courseCode=CS501",
                service.getAuditLog().get(0)
        );
    }

    @Test
    void shouldNotCreateAuditEntryForRejectedRegistration() {
        Student student = new Student("S202", "Rahul");
        Course course = new Course(
                "CS502", "Computer Networks", 3, 10, false, null
        );

        RegistrationService service = new RegistrationService();

        assertFalse(service.register(student, course));
        assertTrue(service.getAuditLog().isEmpty());
        assertEquals(0, course.getCurrentEnrollment());
        assertEquals(0, student.getTotalCredits());
    }

    @Test
    void shouldPreserveAuditEntryAfterLaterRegistrationFails() {
        Student firstStudent = new Student("S203", "Priya");
        Student secondStudent = new Student("S204", "Arun");

        Course course = new Course(
                "CS503", "Operating Systems", 4, 1, true, null
        );

        RegistrationService service = new RegistrationService();

        // First registration succeeds and creates one entry.
        assertTrue(service.register(firstStudent, course));
        assertEquals(1, service.getAuditLog().size());

        String originalEntry = service.getAuditLog().get(0);

        // The second registration is rejected because the course is full.
        assertFalse(service.register(secondStudent, course));

        // The original audit entry must remain unchanged.
        assertEquals(1, service.getAuditLog().size());
        assertEquals(originalEntry, service.getAuditLog().get(0));
        assertEquals(
                "studentId=S203, courseCode=CS503",
                service.getAuditLog().get(0)
        );

        assertEquals(1, course.getCurrentEnrollment());
        assertEquals(4, firstStudent.getTotalCredits());
        assertEquals(0, secondStudent.getTotalCredits());
    }

}