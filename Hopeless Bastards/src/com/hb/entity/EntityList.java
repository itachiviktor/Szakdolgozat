package com.hb.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityList<X extends Entity> {
	
	/*Azért hoztam létre ezt az osztályt,mert így a listaszerkezet Enity minden leszármazottját tudja tárolni,másként nem lehet,
	 ezt az osztályt Tilera is létrehoztam(a probléma az volt,hogy A List<Tile> az csak Tile vagy annak megvalósítását tudja
	 tárolni(megvalósítás az ,hogy abstarct metódusokat megvalósítja), na ezzel a megoldással bármilyen mély leszármazást
	 megoldhatok)*/
	public List<X> list = new ArrayList<X>();
	
	public void add(X x){
		list.add(x);
	}
	
	public void remove(X x){
		list.remove(x);
		
	}
	
	public int size(){
		return list.size();
	}
	
	public X get(int index){
		return list.get(index);
	}
	
	public void clear(){
		list.clear();
	}
	
	public void set(int index,X element){
		list.set(index, element);
	}
	
	public int indexOf(X x){
		return list.indexOf(x);
	}

}
