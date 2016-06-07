package com.hb;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.hb.gamestate.GameState;

public class GameStateList{
	/*Ez egy lista osztály , ami GameState játékállásokat tartalmaz(mindíg csak az aktuális van benne)*/
	
	private List<GameState> list;
	private PropertyChangeSupport sup;
	
	private GameState old;
	
	public GameStateList() {
		list = new ArrayList<GameState>();
		sup = new PropertyChangeSupport(this);
		
	}
	
	public void add(GameState state){
		/*Ha hozzáadnak a listához egy elemet,akkor a már benne lévõt kitöröljük,és csak úgy adunk hozzá,
		 hogy mindíg az aktuális legyen benne.Majd mivel az újat hozzáadtuk,ezért értesítem a figyelõt, ami a GameStateManager
		 osztályom,ami Canvas leszármazott, így az új GameState eseménykezelõit be tudja állítani  Canvashez.*/
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