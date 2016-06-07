package com.hb.entity;

import java.util.ArrayList;
import java.util.List;

import com.hb.tile.Tile;

public class TileList<X extends Tile> {
	
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
	
}
