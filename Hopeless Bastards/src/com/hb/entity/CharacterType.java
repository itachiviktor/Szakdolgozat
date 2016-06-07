package com.hb.entity;

public enum CharacterType {
	MUSCLEMAN,MAGE;
	
	@Override
	public String toString() {
		if(this == CharacterType.MUSCLEMAN){
			return "MUSCLEMAN";
		}else if(this == CharacterType.MAGE){
			return "MAGE";
		}
		return null;
	}
}
