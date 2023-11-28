package com.example.academyadvise_sim.threads;

import com.example.academyadvise_sim.models.Student;
import com.example.academyadvise_sim.models.ExitMonitor;
import com.example.academyadvise_sim.models.enums.StudentState;

public class CounterToEat implements Runnable {
    private ExitMonitor exitMonitor;
    private Student student;

    public CounterToEat(ExitMonitor exitMonitor, Student student) {
        this.exitMonitor = exitMonitor;
        this.student = student;
    }

    @Override
    public void run() {
        while (student.getTime()>0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            student.decrementTime();
        }
        student.setState(StudentState.EAT_FINISH);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exitMonitor.passToExitQueue();
    }
}
