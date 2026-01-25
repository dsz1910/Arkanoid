import java.awt.*;
import java.awt.geom.*;


class Cegielka extends Rectangle2D.Float
{
    Color kolor;

    Cegielka(int x, int y, Color kol)
    {
        super(x, y, 70, 25);
        this.kolor = kol;
    }

    void setX(int x)
    {
        this.x=x;
    }
    void setY(int y)
    {
        this.y=y;
    }
}