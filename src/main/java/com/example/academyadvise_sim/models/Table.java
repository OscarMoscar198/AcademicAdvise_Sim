package com.example.academyadvise_sim.models;

import com.example.academyadvise_sim.models.enums.TableState;

public class Table {
    private Student student;
    private TableState state;

    public Table(Student student) {
        this.student = student;
        this.state=TableState.EMPTY;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public TableState getState() {
        return state;
    }

    public void setState(TableState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Table{" +
                "student=" + student +
                ", state=" + state +
                '}';
    }
}
