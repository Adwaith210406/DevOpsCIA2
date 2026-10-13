package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void shouldCreateActiveCourse() {

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        assertEquals("CS301", course.getCourseCode());
        assertEquals("Data Structures", course.getCourseName());
        assertEquals(4, course.getCredits());
        assertEquals(30, course.getCapacity());
        assertEquals(0, course.getCurrentEnrollment());
        assertTrue(course.isActive());
    }

    @Test
    void shouldCreateInactiveCourse() {

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                25,
                false,
                "CS301"
        );

        assertFalse(course.isActive());
        assertEquals("CS301", course.getPrerequisite());
    }

    @Test
    void enrollmentShouldIncrease() {

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        course.incrementEnrollment();

        assertEquals(1, course.getCurrentEnrollment());
    }
}