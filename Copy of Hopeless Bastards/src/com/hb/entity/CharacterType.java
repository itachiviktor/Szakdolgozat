package com.hb.entity;

public enum CharacterType {
	MAGE(0),MUSCLEMAN(1);
	
	private int number;
	
	private CharacterType(int number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		if(this == CharacterType.MAGE){
			return "MAGE";
		}else if(this == CharacterType.MUSCLEMAN){
			return "MUSCLEMAN";
		}
		return null;
	}
}
