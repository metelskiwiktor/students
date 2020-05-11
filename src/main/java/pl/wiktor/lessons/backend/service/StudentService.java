package pl.wiktor.lessons.backend.service;

import pl.wiktor.lessons.backend.model.Student;
import pl.wiktor.lessons.backend.http.HttpClientServiceStudent;

import java.util.*;

class StudentService {
    private HttpClientServiceStudent httpClientServiceStudent;
    private List<Student> students;

    StudentService() {
        this.httpClientServiceStudent = new HttpClientServiceStudent();
        this.students = httpClientServiceStudent.getAll();
    }

    void addStudent(Student student){
        students = httpClientServiceStudent.add(student);
    }

    List<Student> getStudents() {
        return students;
    }

    void edit(Student student) {
        students = httpClientServiceStudent.update(student);
    }
}
