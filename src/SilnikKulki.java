class SilnikKulki extends Thread
{
    Kulka kulka;
    Plansza plansza;

    SilnikKulki(Kulka k, Plansza p)
    {
        this.kulka = k;
        this.plansza = p;
        start();
    }

    public void run()
    {
        try
        {
            while(true) {
                if (plansza.odliczanie > 0) {
                    plansza.repaint();
                    Thread.sleep(750);
                    plansza.odliczanie--;
                    continue;
                }

                if (!plansza.wygrana && !plansza.gameOver) {
                    kulka.nextKrok();
                    sleep(1);
                }
                else {
                    plansza.repaint();
                }
            }
        }
        catch(InterruptedException e){}
    }
}