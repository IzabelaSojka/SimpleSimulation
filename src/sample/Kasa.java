package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Kasa extends Thread {
    int id;
    String text = "";
    volatile Stacja stacja;
    public static int wykon2 = 0;
    Random rand = new Random();
    @FXML
    Rectangle kwadrat;
    @FXML
    Label kom;

    public Kasa(int id, Stacja stacja, Rectangle kwadrat, Label kom) {
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
        placenie();
    }

    public void placenie() {
        while (true) {
            czekaj();
            Samochod samochod = stacja.zaplac_wyjedz(id);
            pl(samochod.id);
        }
    }

    public void pl(int id) {
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