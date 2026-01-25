import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


class Plansza extends JPanel implements MouseMotionListener
{
    Random rand = new Random();
    int[] wagiKolorow = {20, 35, 45};
    Color[] kolory = {Color.RED, Color.BLUE, Color.GREEN};
    ArrayList<Cegielka> cegielki = new ArrayList<>();

    Integer[] mozliweBonusy = {0, 10, -10, 20, -20, 30, -30, 50, -50};
    int[] wagiBonusow = {300000, 20, 20, 15, 15, 10, 10, 5, 5};
    int wartoscBonusu = 0;
    Bonus bonus;

    Belka belka;
    Kulka kulka;
    SilnikKulki silnikKulki;

    int punkty = 0;
    int zycie = 2;
    boolean gameOver = false;
    boolean wygrana = false;
    int odliczanie = 3;

    Plansza() {
        super();
        addMouseMotionListener(this);

        belka = new Belka(407 - 75);
        kulka = new Kulka(this, 400f, 250f, 0f, 1f);
        silnikKulki = new SilnikKulki(kulka, this);

        synchronized (cegielki) {
            cegielki = budowanieCegielek();
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((gameOver || wygrana) && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartGame();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        String tekst;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.WHITE);

        g2d.fill(kulka);
        g2d.fill(belka);

        synchronized (cegielki) {
            for (Cegielka c : cegielki) {
                g2d.setColor(c.kolor);
                g2d.fill(c);
            }
        }

        if (bonus != null && wartoscBonusu != 0) {
            g2d.setColor(Color.RED);
            g2d.fillRect((int)bonus.x, (int)bonus.y, 30, 30);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 15));

            tekst = String.valueOf(wartoscBonusu);
            FontMetrics fm = g2d.getFontMetrics();
            int szerTekstu = fm.stringWidth(tekst);
            int wysTekstu = fm.getAscent();

            int tekstX = (int)bonus.x + (30 - szerTekstu) / 2;
            int tekstY = (int)bonus.y + (30 + wysTekstu) / 2;

            g2d.drawString(tekst, tekstX, tekstY);
        }

        if (odliczanie > 0) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            g2d.drawString(String.valueOf(odliczanie), getWidth() / 2, getHeight() / 2);
        }

        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setColor(new Color(0, 100, 255));
        g2d.drawString("Punkty " + punkty, 10, 500);
        g2d.setColor(Color.RED);
        g2d.drawString("Życia " + zycie, 600, 500);

        if (gameOver || wygrana) {
            if (gameOver) {
                g2d.setFont(new Font("Arial", Font.BOLD, 60));
                tekst = "GAME OVER";
            }
            else {
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                tekst = "WYGRAŁEŚ!!! ZDOBYŁEŚ " + punkty + " PUNKTÓW";
            }

            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.RED);

            FontMetrics fm = g2d.getFontMetrics();

            int x = (getWidth() - fm.stringWidth(tekst)) / 2;
            int y = getHeight() / 2;
            g2d.drawString(tekst, x, y);

            g2d.setFont(new Font("Arial", Font.PLAIN, 30));
            String sub = "Naciśnij ENTER aby zagrać ponownie";
            x = (getWidth() - g2d.getFontMetrics().stringWidth(sub)) / 2;
            g2d.drawString(sub, x, y + 50);
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        belka.setX(e.getX()-50);
        repaint();
    }

    public void mouseDragged(MouseEvent e) {}

    public ArrayList<Cegielka> budowanieCegielek(){
        int x;
        int y = 10;
        for (int i = 0; i <= 3; i++) {
            x = 0;

            for (int j = 0; j <= 9; j++) {
                x += 5;
                Color kolor = wyborNaPodstawiePrawdopodobienstwa(kolory, wagiKolorow, rand);
                cegielki.add(new Cegielka(x, y, kolor));
                x += 75;
            }
            y += 35;
        }
        return cegielki;
    }
    public void restartGame() {
        zycie = 2;
        punkty = 0;
        kulka.reset();
        belka.reset();
        odliczanie = 3;
        bonus = null;
        wartoscBonusu = 0;
        synchronized (cegielki) {
            cegielki.clear();
            budowanieCegielek();
        }
        gameOver = false;
        wygrana = false;
        repaint();
    }

    public <T> T wyborNaPodstawiePrawdopodobienstwa(T[] tablica, int[] wagi, Random rand) {
        int sumaWag = 0;
        for (int w : wagi) {
            sumaWag += w;
        }

        int r = rand.nextInt(sumaWag);

        int obecnyWybor = 0;
        for (int i = 0; i < tablica.length; i++) {
            obecnyWybor += wagi[i];
            if (obecnyWybor > r) return tablica[i];
        }
        return null;
    }
}