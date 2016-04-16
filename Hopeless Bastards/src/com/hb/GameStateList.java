package com.hb;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.hb.gamestate.GameState;

public class GameStateList{
	/*Ez egy lista oszt�ly , ami GameState j�t�k�ll�sokat tartalmaz(mind�g csak az aktu�lis van benne)*/
	
	private List<GameState> list;
	private PropertyChangeSupport sup;
	
	private GameState old;
	
	public GameStateList() {
		list = new ArrayList<GameState>();
		sup = new PropertyChangeSupport(this);
		
	}
	
	public void add(GameState state){
		/*Ha hozz�adnak a list�hoz egy elemet,akkor a m�r benne l�v�t kit�r�lj�k,�s csak �gy adunk hozz�,
		 hogy mind�g az aktu�lis legyen benne.Majd mivel az �jat hozz�adtuk,ez�rt �rtes�tem a figyel�t, ami a GameStateManager
		 oszt�lyom,ami Canvas lesz�rmazott, �gy az �j GameState esem�nykezel�it be tudja �ll�tani  Canvashez.*/
		if(list.size() > 0){
			old = list.get(0);
			list.remove(0);
			list.add(state);
		}else{
			list.add(state);
		}
		
		sup.firePropertyChange("GameStateChange",old, list.get(0));
	}
	
	public GameState get(int index){
		return list.get(index);
	}
	
	public void registToList(PropertyChangeListener listener){
		sup.addPropertyChangeListener(listener);
	}
	
	public void deleteRegister(PropertyChangeListener listener){
		sup.removePropertyChangeListener(listener);
	}
}