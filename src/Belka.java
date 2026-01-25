import java.awt.geom.*;


class Belka extends Rectangle2D.Float
{
    int startX;

    Belka(int x)
    {
        this.startX = x;
        this.x = x;
        this.y=400;
        this.width=150;
        this.height=20;
    }

    void reset(){
        this.x = this.startX;
    }

    void setX(int x)
    {
        this.x=x;
    }
}