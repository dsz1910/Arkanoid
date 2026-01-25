import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Bonus {
    int wartosc;
    float x, y;
    Random rand;
    Plansza plansza;

    Bonus(int wartosc, Plansza p) {
        this.plansza = p;
        this.wartosc = wartosc;
        this.rand = new Random();
        this.x = rand.nextInt(780);
        this.y = 200;
    }

    void nextKrok() {
        this.y += 0.2f;
    }
}
