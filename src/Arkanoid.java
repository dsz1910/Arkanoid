import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;

public class Arkanoid
{
    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                Plansza p;
                
                p=new Plansza();

                JFrame jf=new JFrame();
                jf.add(p);

                jf.setTitle("Arkanoid");
                jf.setSize(815,600);
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.setVisible(true);
            }
        });
    }
}
