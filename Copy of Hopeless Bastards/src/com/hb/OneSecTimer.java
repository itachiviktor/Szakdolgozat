package com.hb;

public class OneSecTimer extends Thread{
	
	/*Ennek az oszt�lynak csak annyi a szerepe,hogy m�sodpercenk�nt n�veli a m�sodpercsz�ml�l�t.
	 Ezt egy k�l�n sz�lon ind�tom, �s v�gtelen ciklusk�nt fut ott,1 m�sodpercenk�nt altatva mag�t, �s fel�bred�skor 
	 �ll�t egyet a m�sodpercssz�ml�l�n.*/
	
	@Override
	public void run() {
		while(true){
			Game.maintime++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
