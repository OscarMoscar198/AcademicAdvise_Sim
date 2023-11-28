package com.example.academyadvise_sim.models;

import com.example.academyadvise_sim.models.enums.StudentState;
import com.example.academyadvise_sim.models.enums.TableState;

import java.util.Arrays;

public class Classroom {
    private Table[]tables;

    public Classroom() {
        this.tables = new Table[20];
        for (int i = 0; i < this.tables.length; i++) {
            this.tables[i] = new Table(null);
        }
    }

    public boolean isFull(){
        System.out.println(Arrays.toString(this.tables));
        for (Table table : this.tables) {
            if (TableState.EMPTY.equals(table.getState()))
                return false;
        }
        return true;
    }

    public void setData(Student student){
        for (int i = 0; i < this.tables.length; i++) {
            if(TableState.EMPTY.equals(this.tables[i].getState())) {
                student.setTableId(i);
                this.tables[i].setStudent(student);
                this.tables[i].setState(TableState.BUSY);
                return;
            }
        }
    }

    public Student getDinnerByState(StudentState state){
        for (int i = 0; i < this.tables.length; i++) {
            if(TableState.BUSY.equals(this.tables[i].getState())) {
                if(this.tables[i].getStudent().getState().equals(state)){
                    System.out.println("encontre1: "+this.tables[i].toString());
                    return  this.tables[i].getStudent();
                }
            }
        }
        return null;
    }

    public void removeDinnerByTableId(int id){
        this.tables[id].setStudent(null);
        this.tables[id].setState(TableState.EMPTY);
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "tables=" + Arrays.toString(tables) +
                '}';
    }

}
