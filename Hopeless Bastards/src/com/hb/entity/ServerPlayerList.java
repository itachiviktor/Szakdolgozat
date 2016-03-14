package com.hb.entity;

import java.util.ArrayList;
import java.util.List;

public class ServerPlayerList {
	public List<Player> list = new ArrayList<Player>();
	
	public void add(Player x){
		list.add(x);
	}
	
	public void remove(Player x){
		list.remove(x);
		
	}
	
	public Player getById(String id){
		Player en = null;
		for(int i=0;i<list.size();i++){
			en = list.get(i);
			if(en.networkId.equals(id)){
				
				return en;
			}
		}
		return null;
	}
	
	public void removeById(String id){
		Player p = null;
		int count = 0;
		for(int i=0;i<list.size();i++){
			p = list.get(i);
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
	
	public Player get(int index){
		return list.get(index);
	}
	
	public void clear(){
		list.clear();
	}
	
	public void set(int index,Player element){
		list.set(index, element);
	}
	
	public int indexOf(Player x){
		return list.indexOf(x);
	}
}
