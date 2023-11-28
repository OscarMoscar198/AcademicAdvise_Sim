package com.example.academyadvise_sim.threads;

import com.example.academyadvise_sim.models.StudentMonitor;

import java.util.Observable;

public class ProduceQueueWait  extends Observable implements Runnable{

    private final StudentMonitor studentMonitor;
    public ProduceQueueWait(StudentMonitor studentMonitor){
        this.studentMonitor = studentMonitor;
    }
    @Override
    public void run() {
        while (true){
            this.studentMonitor.generateStudentsWait();
            setChanged();
            notifyObservers("1");
        }
    }
}
