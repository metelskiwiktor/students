package pl.wiktor.lessons.backend.http;

import pl.wiktor.lessons.backend.model.Student;

public class HttpClientServiceStudent extends HttpClientService<Student> {
    private static final String STUDENTS_ADDRESS = "http://localhost:8085/students";

    public HttpClientServiceStudent() {
        super(STUDENTS_ADDRESS, Student.class);
    }
}
