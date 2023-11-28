package com.example.academyadvise_sim.threads;

import com.example.academyadvise_sim.models.TutorMonitor;

import java.util.Observable;

public class ConsumeCommands  extends Observable implements Runnable{
    private TutorMonitor tutorMonitor;

    public ConsumeCommands(TutorMonitor tutorMonitor) {
        this.tutorMonitor = tutorMonitor;
    }

    @Override
    public void run() {
        while(true){
            this.tutorMonitor.makeTheOrder();
            setChanged();
            notifyObservers("3");
        }
    }
}
