package com.example.academyadvise_sim.models;

import com.example.academyadvise_sim.models.enums.StudentState;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class ExitMonitor {
    private Deque<Student> exitQueue;
    private Classroom classroom;
    private Student exit;

    public ExitMonitor( Classroom classroom) {
        this.exitQueue = new LinkedList<Student>();
        this.classroom = classroom;
    }
    public synchronized void extractStudentsExit(){
        while (exitQueue.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.exit=this.exitQueue.getFirst();
        this.exit.setState(StudentState.EXIT);
        classroom.removeDinnerByTableId(exit.getTableId());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public synchronized void passToExitQueue(){
        while (exitQueue.size()==20) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
            Student studentOrder = classroom.getDinnerByState(StudentState.EAT_FINISH);
            if(studentOrder !=null){
                studentOrder.setState(StudentState.WAIT_ORDER);
                this.exitQueue.add(studentOrder);
            }
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public Student removeFromExitQueue(){
        return this.exitQueue.remove();
    }
}