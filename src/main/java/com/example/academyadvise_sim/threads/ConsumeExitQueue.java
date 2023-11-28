package com.example.academyadvise_sim.threads;

import com.example.academyadvise_sim.models.ExitMonitor;

import java.util.Observable;

public class ConsumeExitQueue extends Observable implements Runnable{
    private ExitMonitor exitMonitor;

    public ConsumeExitQueue(ExitMonitor exitMonitor) {
        this.exitMonitor = exitMonitor;
    }

    @Override
    public void run() {
        while (true){
            exitMonitor.extractStudentsExit();
            setChanged();
            notifyObservers("4");
        }

    }
}
