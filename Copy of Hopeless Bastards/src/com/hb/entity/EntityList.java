package com.hb.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityList<X extends Entity> {
	
	/*Az�rt hoztam l�tre ezt az oszt�lyt,mert �gy a listaszerkezet Enity minden lesz�rmazottj�t tudja t�rolni,m�sk�nt nem lehet,
	 ezt az oszt�lyt Tilera is l�trehoztam(a probl�ma az volt,hogy A List<Tile> az csak Tile vagy annak megval�s�t�s�t tudja
	 t�rolni(megval�s�t�s az ,hogy abstarct met�dusokat megval�s�tja), na ezzel a megold�ssal b�rmilyen m�ly lesz�rmaz�st
	 megoldhatok)*/
	public List<X> list = new ArrayList<X>();
	private Player en;
	private Player p;
	
	public void add(X x){
		list.add(x);
	}
	
	public void remove(X x){
		list.remove(x);
		
	}
	
	public X getById(String id){
		en = null;
		for(int i=0;i<list.size();i++){
			en = (Player)list.get(i);
			if(en.networkId.equals(id)){
				
				return (X) en;
			}
		}
		return null;
	}
	
	public void removeById(String id){
		p = null;
		int count = 0;
		for(int i=0;i<list.size();i++){
			p = (Player)list.get(i);
			if(p.networkId.equals(id)){
				count = i;
				break;
				
			}
		}
		list.remove(count);
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
