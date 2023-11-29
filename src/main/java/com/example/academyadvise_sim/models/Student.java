package com.example.academyadvise_sim.models;
import com.example.academyadvise_sim.models.enums.StudentState;
import javafx.scene.paint.Color;

public class Student {
    private StudentState state;
    private int id;
    private  int tableId;
    private Color color;
    private int time;

    public Student(int id){
        this.id=id;
        this.time=0;
        this.color=GenerateColorRandom();
        this.state=StudentState.WAIT;
        this.tableId=-1;
    }

    private Color GenerateColorRandom(){
        int rangR = (int)(Math.random() * (124 - 190)) + 170;
        int rangG = (int)(Math.random() * (124 - 190)) + 170;
        int rangB = (int)(Math.random() * (124 - 190)) + 170;
        return Color.rgb(rangR, rangG, rangB);
    }

    public Color getColor() {
        return color;
    }

    public int getTime() {
        return time;
    }

    public StudentState getState() {
        return state;
    }

    public void setState(StudentState state) {
        this.state = state;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "state=" + state +
                ", id=" + id +
                ", tableId=" + tableId +
                ", color=" + color +
                ", time=" + time +
                '}';
    }

    public void decrementTime(){
        this.time--;
    }
}
