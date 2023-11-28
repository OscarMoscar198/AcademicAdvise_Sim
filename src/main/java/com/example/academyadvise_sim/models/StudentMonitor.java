package com.example.academyadvise_sim.models;

import com.example.academyadvise_sim.models.enums.StudentState;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class StudentMonitor {
    private Deque<Student> queue_wait;
    private Classroom classroom;
    private Student enter;
    private int total;
    private int id;

    @Override
    public String toString() {
        return "StudentMonitor{" +
                "queue_wait=" + queue_wait +
                ", total=" + total +
                '}';
    }

    public StudentMonitor(int total, Classroom classroom){
        this.queue_wait= new LinkedList<Student>();
        this.classroom = classroom;
        this.enter=null;
        this.total=total;
        this.id=20;
    }

    public synchronized void generateStudentsWait(){
        while (total == queue_wait.size()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Student student = new Student(id);
        this.id++;
        queue_wait.add(student);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public synchronized void extractStudentsWait(){
        while (queue_wait.size() == 0 || classroom.isFull()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.enter=this.queue_wait.getFirst();
        this.enter.setState(StudentState.SIT_WITHOUT_ORDER);
        this.classroom.setData(this.getEnter());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.queue_wait.removeFirst();
        this.notifyAll();
    }

    public Student getEnter() { return this.enter; }

    public Deque<Student> getQueue_wait() {return queue_wait;}

}
