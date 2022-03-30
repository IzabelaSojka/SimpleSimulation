package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.io.IOException;

import javafx.concurrent.Task;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Controller {
    @FXML
    Label komunikat;
    @FXML
    TextField liczba;
    @FXML
    TextField max;
    @FXML
    Label czekT;
    @FXML
    Label czekP;
    @FXML
    Label naStacji;
    @FXML
    Label kom1;
    @FXML
    Label kom2;
    @FXML
    Label kom11;
    @FXML
    Label kom12;
    @FXML
    Label kom13;
    @FXML
    Label kom14;
    @FXML
    Rectangle kwadrat01;
    @FXML
    Rectangle kwadrat02;
    @FXML
    Rectangle kwadrat03;
    @FXML
    Rectangle kwadrat04;
    @FXML
    Rectangle kwadrat11;
    @FXML
    Rectangle kwadrat12;
    Stacja stacja;
    Kasa[] kasy;
    Stanowisko[] stanowiska;
    Wjazd wjazd;

    public void Start(ActionEvent event) throws IOException {

        String Ile = liczba.getText();
        String Max = max.getText();
        int maks = 0;
        int ile = 0;

        try (InputStream input = new FileInputStream("C:/Users/sojka/Desktop/Projekt/src/dane/dane.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            maks = Integer.parseInt(prop.getProperty("maksymalna"));
            ile = Integer.parseInt(prop.getProperty("liczba"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Max.length() != 0) {
            maks = Integer.parseInt(Max);
        }

        if (Ile.length() != 0) {
            ile = Integer.parseInt(Ile);
        }

        int maks1 = maks;
        int ile1 = ile;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                uruchom(maks1, ile1);
                return null;
            }
        };
        new Thread(task).start();
    }

    public void zamknij(ActionEvent event) throws IOException {
        System.exit(0);
    }

    private void uruchom(int maks, int ile) throws IOException {

        stacja = new Stacja(maks, czekT, czekP, naStacji);
        wjazd = new Wjazd(ile, stacja, komunikat);

        stanowiska = new Stanowisko[4];
        kasy = new Kasa[2];

        kwadrat01.setFill(Color.GREEN);
        kwadrat02.setFill(Color.GREEN);
        kwadrat03.setFill(Color.GREEN);
        kwadrat04.setFill(Color.GREEN);

        stanowiska[0] = new Stanowisko(1, stacja, kwadrat01, kom11);
        stanowiska[1] = new Stanowisko(2, stacja, kwadrat02, kom12);
        stanowiska[2] = new Stanowisko(3, stacja, kwadrat03, kom13);
        stanowiska[3] = new Stanowisko(4, stacja, kwadrat04, kom14);

        kwadrat11.setFill(Color.GREEN);
        kwadrat12.setFill(Color.GREEN);

        kasy[0] = new Kasa(1, stacja, kwadrat11, kom1);
        kasy[1] = new Kasa(2, stacja, kwadrat12, kom2);

        wjazd.start();

        for (int i = 0; i < 4; i++) {
            stanowiska[i].start();
        }
        for (int i = 0; i < 2; i++) {
            kasy[i].start();
        }
        try {
            wjazd.join();
        } catch (InterruptedException e) {
        }

        for (int i = 0; i < 4; i++) {
            try {
                stanowiska[i].join();
            } catch (InterruptedException e) {
            }
        }
        for (int i = 0; i < 2; i++) {
            try {
                kasy[i].join();
            } catch (InterruptedException e) {
            }
        }
    }
}
