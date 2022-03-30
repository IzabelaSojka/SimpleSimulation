package sample;

import java.util.Random;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Stanowisko extends Thread {
    int id;
    volatile Stacja stacja;
    String text = "";
    Random rand = new Random();

    @FXML
    Rectangle kwadrat;
    @FXML
    Label kom;

    public Stanowisko(int id, Stacja stacja, Rectangle kwadrat, Label kom) {
        this.id = id;
        this.stacja = stacja;
        this.kwadrat = kwadrat;
        this.kom = kom;
    }

    public void delay(int a, int b) {
        try {
            Thread.sleep(rand.nextInt(a) + b);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void czekaj() {
        delay(100, 100);
    }

    public void run() {
        tankowanie();
    }

    public void tankowanie() {
        while (true) {
            czekaj();
            Samochod samochod = stacja.tankuj(id);
            tank(samochod.id);
        }
    }

    public void tank(int id) {
        this.kwadrat.setFill(Color.RED);
        Platform.runLater(() -> {
            kom.setText(Integer.toString(id));
        });
        delay(1000, 1000);
        this.kwadrat.setFill(Color.GREEN);
        Platform.runLater(() -> {
            this.kom.setText(text);
        });
    }
}

