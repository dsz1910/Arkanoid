import java.awt.*;
import java.awt.geom.*;
import java.util.Iterator;

class Kulka extends Ellipse2D.Float
{
    Plansza plansza;
    float dx, dy;
    float startX, startY;

    Kulka(Plansza plansza, float x, float y, float dx, float dy)
    {
        super(x, y, 35, 35);
        this.startX = x;
        this.startY = y;

        this.plansza = plansza;
        this.dx=dx;
        this.dy=dy;
    }


    public void reset(){
        x = startX;
        y = startY;
        dy = Math.abs(dy);
    }

    void nextKrok()
    {
        int zdobytePunkty = 0;
        x+=dx;
        y+=dy;

        if (plansza.bonus == null) {
            plansza.wartoscBonusu = plansza.wyborNaPodstawiePrawdopodobienstwa(
                    plansza.mozliweBonusy, plansza.wagiBonusow, plansza.rand);
            if (plansza.wartoscBonusu != 0) {
                plansza.bonus = new Bonus(plansza.wartoscBonusu, plansza);
            }
        }
        else {
            plansza.bonus.nextKrok();

            if (plansza.belka.intersects(plansza.bonus.x, plansza.bonus.y, 30, 30)) {
                plansza.punkty += plansza.bonus.wartosc;
                plansza.bonus = null;
                plansza.wartoscBonusu = 0;
            }

            else if (plansza.bonus.y >= plansza.getHeight()) {
                plansza.bonus = null;
                plansza.wartoscBonusu = 0;
            }
        }

        if(getMinX()<0 || getMaxX()> plansza.getWidth())  dx=-dx;

        if(getMinY()<0) dy= -dy;

        synchronized (plansza.cegielki) {
            if (plansza.cegielki.isEmpty()) {
                plansza.wygrana = true;
            }
        }

        if(getMaxY()> plansza.getHeight() && plansza.getHeight() > 0) {
            this.reset();
            plansza.zycie -= 1;
            plansza.wartoscBonusu = 0;
            plansza.bonus = null;
            if (plansza.zycie <= 0) plansza.gameOver = true;
            plansza.odliczanie = 3;
            dx = 0;
        }

        if (plansza.belka.intersects(this.getBounds2D())) {
            Rectangle2D intersection = plansza.belka.createIntersection(this.getBounds2D());

            if (intersection.getWidth() > intersection.getHeight()) {
                dy = -dy;

                float xSrodkaPilki = x + width / 2f;
                float xSrodkaBelki = plansza.belka.x + plansza.belka.width / 2f;

                float roznicaSrodkow = xSrodkaPilki - xSrodkaBelki;
                float znromalizowanaRoznicaSrodkow = roznicaSrodkow / (plansza.belka.width / 2f);

                float maksPredkoscPozioma = Math.abs(dy);

                dx = znromalizowanaRoznicaSrodkow * maksPredkoscPozioma;

                if (getMinY() < plansza.belka.y) {
                    y = plansza.belka.y - height;
                } else {
                    y = plansza.belka.y + plansza.belka.height;
                }
            } else {
                dx = -dx;
            }
        }

        synchronized (plansza.cegielki) {
            Iterator<Cegielka> it = plansza.cegielki.iterator();
            while (it.hasNext()) {
                Cegielka c = it.next();
                if (this.getBounds2D().intersects(c.getBounds2D())) {
                    if (c.kolor == Color.RED) zdobytePunkty = 30;
                    if (c.kolor == Color.BLUE) zdobytePunkty = 20;
                    if (c.kolor == Color.GREEN) zdobytePunkty = 10;

                    it.remove();
                    plansza.punkty += zdobytePunkty;
                    dy = -dy;
                    break;
                }
            }
        }
        plansza.repaint();
        Toolkit.getDefaultToolkit().sync();
    }
}