package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Random;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Stacja extends Thread {
    volatile Samochod samochody[];
    volatile Samochod samochody1[];
    volatile Samochod samochody2[];
    volatile int wjazd, wjazd1, wjazd2;
    volatile int wyjazd, wyjazd1, wyjazd2;
    volatile int licznik, licznik1, licznik2;
    int rozmiar;
    Random rand = new Random();

    @FXML
    Label czekT;
    @FXML
    Label czekP;
    @FXML
    Label naStacji;

    final Lock dostep;
    final Condition pelny;
    final Condition pusty;

    public void delay(int a, int b) {
        try {
            Thread.sleep(rand.nextInt(a) + b);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    Stacja(int rozmiar, Label czekT, Label czekP, Label naStacji) throws IOException {
        this.rozmiar = rozmiar;
        samochody = new Samochod[rozmiar];
        samochody1 = new Samochod[rozmiar];
        samochody2 = new Samochod[rozmiar];
        wjazd = 0;
        wjazd1 = 0;
        wyjazd1 = 0;
        wjazd2 = 0;
        wyjazd2 = 0;
        licznik = 0;
        licznik1 = 0;
        licznik2 = 0;
        dostep = new ReentrantLock();
        pelny = dostep.newCondition();
        pusty = dostep.newCondition();
        this.czekT = czekT;
        this.czekP = czekP;
        this.naStacji = naStacji;
    }

    public void wjedz(int licz) {
        Samochod samochod = new Samochod(licz);
        dostep.lock();
        try {
            if (licznik == rozmiar) {
                try {
                    pelny.await();
                } catch (Exception e) {
                }
            }
            System.out.println("Na stacje wjechał samochod :" + samochod.id);
            samochody[wjazd] = samochod;
            wjazd = (wjazd + 1) % rozmiar;
            samochody1[wjazd1] = samochod;
            wjazd1 = (wjazd1 + 1) % rozmiar;
            licznik++;
            licznik1++;
            Platform.runLater(() -> {
                naStacji.setText(Integer.toString(licznik));
                czekT.setText(Integer.toString(licznik1));
            });
            delay(100, 100);
            pusty.signal();

        } finally {
            dostep.unlock();
        }
    }

    public Samochod tankuj(int nr) {
        dostep.lock();
        Samochod samochod;
        delay(100, 100);
        try {
            if (licznik1 == 0) {
                try {
                    pusty.await();
                } catch (Exception e) {
                }
            }
            samochod = samochody1[wyjazd1];
            wyjazd1 = (wyjazd1 + 1) % rozmiar;
            samochody2[wjazd2] = samochod;
            wjazd2 = (wjazd2 + 1) % rozmiar;
            licznik2++;
            licznik1--;
            Platform.runLater(() -> {
                czekT.setText(Integer.toString(licznik1));
                czekP.setText(Integer.toString(licznik2));
            });
            System.out.println("Stanowisko " + nr + ": samochod :" + samochod.id);
            delay(100, 100);
            pelny.signal();
        } finally {
            dostep.unlock();
        }
        return samochod;
    }

    public Samochod zaplac_wyjedz(int nr) {
        dostep.lock();
        Samochod samochod;
        delay(100, 100);
        try {
            if (licznik2 == 0) {
                try {
                    pusty.await();

                } catch (Exception e) {
                }
            }
            samochod = samochody2[wyjazd2];
            wyjazd2 = (wyjazd2 + 1) % rozmiar;
            licznik2--;
            licznik--;
            Platform.runLater(() -> {
                czekP.setText(Integer.toString(licznik1));
            });
            System.out.println("Kasa " + nr + ": samochod :" + samochod.id);

            Platform.runLater(() -> {
                naStacji.setText(Integer.toString(licznik));
            });
            delay(100, 100);
            pelny.signal();
        } finally {
            dostep.unlock();
        }
        return samochod;
    }
}
