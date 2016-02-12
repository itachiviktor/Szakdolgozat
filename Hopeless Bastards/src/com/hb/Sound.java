package com.hb;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	/*Hang lej�tsz�s�rt felel� oszt�ly.Nagyon nem vagyok ott a Sound API-ban, annyit �rdemes tudni az oszt�lyr�l,
	 hogy csak wav kiterjeszt�s� hangfileokat kezel.
	 3 met�dusa van, play,close,stop*/
	
	private Clip clip;
	
	public Sound(String path) {
		try{
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED,baseFormat.getSampleRate(),16,baseFormat.getChannels(),
					baseFormat.getChannels()*2,baseFormat.getSampleRate(),false);
			
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat,ais);
			
			clip = AudioSystem.getClip();
			clip.open(dais);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void play(){
		if(clip == null){
			return;
		}
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void close(){
		stop();
		clip.close();
	}
	
	public void stop(){
		if(clip.isRunning()){
			clip.stop();
		}
	}
	
}
