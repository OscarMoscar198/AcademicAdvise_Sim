package com.example.academyadvise_sim.threads;

import com.example.academyadvise_sim.models.TutorMonitor;
import java.util.concurrent.ThreadLocalRandom;

public class ProduceCommand implements Runnable{
    private TutorMonitor tutorMonitor;

    public ProduceCommand(TutorMonitor tutorMonitor) {
        this.tutorMonitor = tutorMonitor;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            this.tutorMonitor.generatedCommand();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
