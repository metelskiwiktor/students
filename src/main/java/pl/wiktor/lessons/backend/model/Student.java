package pl.wiktor.lessons.backend.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private BigDecimal costPerHouse;
    private String description;

    public Student() {
    }

    public Student(String firstName, String lastName, BigDecimal costPerHouse) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.costPerHouse = costPerHouse;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getCostPerHouse() {
        return costPerHouse;
    }

    public String getFormattedCostPerHouse() {
        return costPerHouse.intValue() + " zł/h";
    }

    public String getName(){
        return String.join(" ", firstName, lastName);
    }

    public void setCostPerHouse(BigDecimal costPerHouse) {
        this.costPerHouse = costPerHouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
