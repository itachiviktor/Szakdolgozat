package com.hb;

public class OneSecTimer extends Thread{
	
	/*Ennek az osztálynak csak annyi a szerepe,hogy másodpercenként növeli a másodpercszámlálót.
	 Ezt egy külön szálon indítom, és végtelen ciklusként fut ott,1 másodpercenként altatva magát, és felébredéskor 
	 állít egyet a másodpercsszámlálón.*/
	
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
