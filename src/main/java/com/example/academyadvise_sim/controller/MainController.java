package com.example.academyadvise_sim.controller;

import com.example.academyadvise_sim.models.*;
import com.example.academyadvise_sim.threads.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Observable;
import java.util.Observer;


public class MainController implements Observer{
    private StudentMonitor studentMonitor;

    private ExitMonitor exitMonitor;

    private Classroom classroom;

    private TutorMonitor tutorMonitor;

    private final Color EmptySpaceColor=Color.web("#232424");


    private final Color WaitresColor=Color.web("#ffffff");

    private final Color TutorColor=Color.web("#7d00ff");

    @FXML
    private Button btn_start;

    @FXML
    private Rectangle enter_waitress;


    @FXML
    private Rectangle add_command;

    @FXML
    private Rectangle cooking_command;

    @FXML
    private Rectangle deliver_command;

    @FXML
    private Rectangle enter_student;

    @FXML
    private Rectangle exit;

    @FXML
    private Rectangle exit_door;

    @FXML
    private HBox queue_wait;

    @FXML
    private Rectangle receive_command;

    @FXML
    private GridPane tables;

    @FXML
    private Rectangle wait_command;
    @FXML
    public void initialize() {
        this.classroom =new Classroom();
        this.studentMonitor =new StudentMonitor(10, this.classroom);
        this.tutorMonitor =new TutorMonitor(this.classroom);
        this.exitMonitor=new ExitMonitor(this.classroom);
    }

    @FXML
    void onClickedStart(MouseEvent event) {
        initSimulation();
    }

    private void initSimulation(){
        ConsumeQueueWait consumeQueueWait= new ConsumeQueueWait(this.studentMonitor);
        ProduceQueueWait produceQueueWait = new ProduceQueueWait(this.studentMonitor);
        ConsumeCommands consumeCommands= new ConsumeCommands(this.tutorMonitor);
        ConsumeExitQueue ConsumeExitQueue = new ConsumeExitQueue(this.exitMonitor);

        consumeQueueWait.addObserver(this);
        produceQueueWait.addObserver(this);
        consumeCommands.addObserver(this);
        ConsumeExitQueue.addObserver(this);

        Thread consumeCommandsThread= new Thread(consumeCommands);
        consumeCommandsThread.setDaemon(true);
        Thread produceQueueWaitThread = new Thread(produceQueueWait);
        produceQueueWaitThread.setDaemon(true);
        Thread consumeQueueWaitThread = new Thread(consumeQueueWait);
        consumeQueueWaitThread.setDaemon(true);
        Thread ConsumeExitQueueThread= new Thread(ConsumeExitQueue);
        ConsumeExitQueueThread.setDaemon(true);

        consumeCommandsThread.start();
        ConsumeExitQueueThread.start();
        produceQueueWaitThread.start();
        consumeQueueWaitThread.start();
    }

    @Override
    public void update(Observable observable, Object o) {
        switch (Integer.valueOf(String.valueOf(o))) {
            case 1:
                addStudentToQueueWait();
                break;
            case 2:
                enterStudentToEntrace();
                waitSecond(2);
                sitStudentAtSomeTable();
                makeOrder();
                break;
            case 3:
                eatTimer();
                break;
            case 4:
                leaveStudent();
                break;
        }
    }

    private void leaveStudent(){
        Student student =exitMonitor.removeFromExitQueue();
        StackPane stackPane= getTable(student.getTableId());
        Rectangle rectangle=(Rectangle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        Platform.runLater(()->{
            text.setText("-");
            text.setFill(Color.WHITE);
            rectangle.setFill(EmptySpaceColor);
            exit_door.setFill(student.getColor());
        });
        waitSecond(2);
        Platform.runLater(()->{
            exit_door.setFill(EmptySpaceColor);
            exit.setFill(student.getColor());
        });
        waitSecond(2);
        Platform.runLater(()->{
            exit.setFill(EmptySpaceColor);
        });

    }

    private void eatTimer(){
        deliverCommand();
        waitSecond(1);
        Student student = tutorMonitor.getOrders().remove();
        StackPane stackPane=getTable(student.getTableId());
        Text text = (Text) stackPane.getChildren().get(1);
        Timeline timeline = new Timeline();
        EventHandler<ActionEvent> eventHandler = event -> {
            Platform.runLater(()->{
                text.setText(String.valueOf(student.getTime()));
            });
            if (student.getTime() == 0) {
                Platform.runLater(()->{
                    text.setText("-F");
                });
                timeline.stop();
            }
        };
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), eventHandler);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        countdown(student);
        waitSecond(1);
        CheckCommands();
    }

    private void CheckCommands(){
        Platform.runLater(()->{
            wait_command.setFill(TutorColor);
            cooking_command.setFill(TutorColor);
            deliver_command.setFill(EmptySpaceColor);
        });
    }

    private void deliverCommand(){
        Platform.runLater(()->{
            deliver_command.setFill(TutorColor);
            cooking_command.setFill(EmptySpaceColor);
            wait_command.setFill(EmptySpaceColor);
        });
    }

    private void countdown(Student student){
        CounterToEat counterEatStudent=new CounterToEat(this.exitMonitor, student);
        Thread CounterToEatThread=new Thread(counterEatStudent);
        CounterToEatThread.setDaemon(true);
        CounterToEatThread.start();
    }
    private void makeOrder(){
        ProduceCommand produceCommand=new ProduceCommand(this.tutorMonitor);
        Thread produceCommandThread=new Thread(produceCommand);
        produceCommandThread.setDaemon(true);
        produceCommandThread.start();
        workTutor();
    }

    private void workTutor(){
        Platform.runLater(()->{
            wait_command.setFill(EmptySpaceColor);
            deliver_command.setFill(EmptySpaceColor);
            cooking_command.setFill(TutorColor);
        });
    }

    private void enterStudentToEntrace(){
        Rectangle popStudent= (Rectangle) queue_wait.getChildren().get(0);
        Platform.runLater(()->{
            enter_student.setFill(popStudent.getFill());
            queue_wait.getChildren().remove(popStudent);
        });
    }
    private void sitStudentAtSomeTable(){
        for(Node hboxNode:tables.getChildren()){
            HBox hbox=(HBox) hboxNode;
            Rectangle waitress = (Rectangle) hbox.getChildren().get(0);
            StackPane stackPane= (StackPane) hbox.getChildren().get(1);
            Rectangle student = (Rectangle) stackPane.getChildren().get(0);
            if(EmptySpaceColor.equals(student.getFill())){
                sitStudent(waitress, stackPane);
                waitSecond(1);
                waitresReturnEntrace(waitress);
                break;
            }
        }
    }

    private void sitStudent( Rectangle waitress,StackPane stackPane){
        Rectangle student = (Rectangle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        Platform.runLater(()->{
            enter_waitress.setFill(EmptySpaceColor);
            waitress.setFill(WaitresColor);
            student.setFill(enter_student.getFill());
            text.setFill(Color.BLACK);
            text.setText("-W");
            enter_student.setFill(EmptySpaceColor);
        });
    }

    private void waitresReturnEntrace(Rectangle waitress){
        Platform.runLater(()->{
            enter_waitress.setFill(WaitresColor);
            waitress.setFill(EmptySpaceColor);
        });
    }
    private void addStudentToQueueWait(){
        Student newStudent =this.studentMonitor.getQueue_wait().getLast();
        Rectangle square = new Rectangle(50, 50, newStudent.getColor());
        square.setArcHeight(30);
        square.setArcWidth(30);
        Platform.runLater(()->{queue_wait.getChildren().add(square);});
    }

    private void waitSecond(int second){
        int milliseconds= second*1000;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private StackPane getTable(int id){
        Node hboxNode=tables.getChildren().get(id);
        HBox hbox=(HBox) hboxNode;
        return  (StackPane) hbox.getChildren().get(1);
    }
}
