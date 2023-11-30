package com.example.academyadvise_sim.models;

import com.example.academyadvise_sim.models.enums.StudentState;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class TutorMonitor {
    private Queue<Student> commands;

    private Deque<Student> orders;

    private Classroom classroom;

    private int TOTAL;

    public TutorMonitor(Classroom classroom){
        this.commands=new LinkedList<Student>();
        this.orders=new LinkedList<Student>();
        this.classroom = classroom;
        this.TOTAL=20;

    }
    public synchronized void makeTheOrder() {
        while (commands.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Student student =commands.remove();
        student.setState(StudentState.EAT);
        student.setTime(getRandomEatTime());
        orders.add(student);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();

    }

    public int getRandomEatTime() {
        return  (int)(Math.random() * (10 - 5)) + 5;
    }

    public synchronized void generatedCommand(){
        while (commands.size()==TOTAL) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Student studentOrder = classroom.getDinnerByState(StudentState.SIT_WITHOUT_ADVICE);
        if(studentOrder !=null){
            studentOrder.setState(StudentState.WAIT_ADVICE);
            commands.add(studentOrder);
        }
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public Deque<Student> getOrders() {
        return orders;
    }
}
