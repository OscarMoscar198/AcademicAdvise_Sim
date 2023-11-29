package com.example.academyadvise_sim.threads;

import com.example.academyadvise_sim.models.StudentMonitor;

import java.util.Observable;

public class ConsumeQueueWait  extends Observable implements Runnable{
    private StudentMonitor studentMonitor;

    public ConsumeQueueWait(StudentMonitor studentMonitor){
        this.studentMonitor = studentMonitor;
    }
    @Override
    public void run() {
        while (true){
            System.out.println("Presente");
            this.studentMonitor.extractStudentsWait();
            setChanged();
            notifyObservers("2");
        }
    }
}
