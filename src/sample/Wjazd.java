package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Random;

public class Wjazd extends Thread {
    Random rand = new Random();
    int liczba;
    int licz;
    volatile Stacja stacja;
    @FXML
    Label komunikat;

    public Wjazd(int liczba, Stacja stacja, Label komunikat) {
        this.liczba = liczba;
        this.stacja = stacja;
        this.licz = 1;
        this.komunikat = komunikat;
    }

    public void delay(int a, int b) {
        try {
            Thread.sleep(rand.nextInt(a) + b);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void czekaj() {
        delay(1000, 100);
    }

    public void run() {
        wjezdzanie();
    }

    public void wjezdzanie() {
        Platform.runLater(() -> {
            this.komunikat.setText("OTWARTE");
        });
        licz = 1;
        while (licz < liczba) {
            if (licz % 21 == 0) {
                Platform.runLater(() -> {
                    this.komunikat.setText("WKROTCE ZAMKNIĘCIE");
                });
                System.out.println("WKROTCE ZAMKNIĘCIE");
                while (stacja.licznik > 0) {
                    ;
                }
                Platform.runLater(() -> {
                    this.komunikat.setText("ZAMKNIĘTE");
                });
                System.out.println("ZAMKNIĘTE");
                for (int i = 0; i < 6; i++) {
                    czekaj();
                }
                Platform.runLater(() -> {
                    this.komunikat.setText("OTWARTE");
                });
                System.out.println("OTWARTE");
            }
            stacja.wjedz(licz);
            licz++;
        }
        while (stacja.licznik > 0) {
        }
        Platform.runLater(() -> {
            this.komunikat.setText("KONIEC");
        });
    }

}

