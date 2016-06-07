package com.hb.textfield;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.RoundRectangle2D;


public class TextField2D{
	private int x1, x2, y1, y2;			// az uj kijeloles helye
	private int oldx1, oldx2, oldy1, oldy2;         // az elozo kijeloles helye
	
	private int xcur, ycur, oldx, oldy;	// a kurzor regi es uj helye
	private StringBuilder sb;
	private TextLayout megjelenito;
	
	private ActionType actionType;
	private int deletedSzovegIndex = 0;
	private int modifySzovegIndex = 0;
	
	private int fontSize;
	private int x;
	private int y;
	private int width;
	private int height;
	private Font font;
	
	private Color cursorColor;
	private Color textFieldColor;
	private Color textColor;
	private Color kijelolColor;
	
	private boolean ownCursor = false;
	
	private static int ownSorszam = 0;
	private static int cursorBirtokos = 0;
	private int sajatSorszam;
	
	private boolean filledDraw;
	private boolean kijeloles;
	
	private Container window = null;
	private Canvas canvas = null;
	
	private int elsoTalalat;
	private int masodikTalalat;
	
	private RoundRectangle2D round = null;
	
	private GradientPaint gp = null;/*színátmenet is megadható a háttérbe*/
	private boolean szinAtmenetes = false;
	private Graphics2D g2;
	
	public TextField2D(String text,Font font,int x,int y,int width,Color cursorColor,Color textFieldColor,Color textColor
			,Color kijelolColor,Container w,boolean filledDraw) {
	
		sb = new StringBuilder();
		xcur = ycur = oldx = oldy = 0;/*belekattintáshoz van*/
		
		x1 = x2 = y1 = y2 = 0;/*kijelöléshez van*/
		oldx1 = oldx2 = oldy1 = oldy2 = 0;
		
		sajatSorszam = ownSorszam;
		ownSorszam++;
		
		sb.append(text);
		this.font = font;
		this.x = x;
		this.y = y;
		this.width = width;
		this.cursorColor = cursorColor;
		this.textFieldColor = textFieldColor;
		this.textColor = textColor;
		this.kijelolColor = kijelolColor;
		this.height = font.getSize() + (int)(font.getSize()/5.0);
		this.window = w;
		
		this.filledDraw = filledDraw;
		
		
	}
	
	public TextField2D(String text,Font font,int x,int y,int width,Color cursorColor,Color textFieldColor,Color textColor
			,Color kijelolColor,Canvas canvas,boolean filledDraw) {
	
		sb = new StringBuilder();
		xcur = ycur = oldx = oldy = 0;/*belekattintáshoz van*/
		
		x1 = x2 = y1 = y2 = 0;/*kijelöléshez van*/
		oldx1 = oldx2 = oldy1 = oldy2 = 0;
		
		sajatSorszam = ownSorszam;
		ownSorszam++;
		
		sb.append(text);
		this.font = font;
		this.x = x;
		this.y = y;
		this.width = width;
		this.cursorColor = cursorColor;
		this.textFieldColor = textFieldColor;
		this.textColor = textColor;
		this.kijelolColor = kijelolColor;
		this.height = font.getSize() + (int)(font.getSize()/5.0);
		this.canvas = canvas;
		
		this.filledDraw = filledDraw;
		
		
	}
	
	public TextField2D(String text,Font font,int x,int y,int width,Color cursorColor,Color textFieldColor,Color textColor
			,Color kijelolColor,Container w,boolean filledDraw,int fok1,int fok2) {
		sb = new StringBuilder();
		xcur = ycur = oldx = oldy = 0;/*belekattintáshoz van*/
		
		x1 = x2 = y1 = y2 = 0;/*kijelöléshez van*/
		oldx1 = oldx2 = oldy1 = oldy2 = 0;
		
		sajatSorszam = ownSorszam;
		ownSorszam++;
		
		sb.append(text);
		this.font = font;
		this.x = x;
		this.y = y;
		this.width = width;
		this.cursorColor = cursorColor;
		this.textFieldColor = textFieldColor;
		this.textColor = textColor;
		this.kijelolColor = kijelolColor;
		this.height = font.getSize() + (int)(font.getSize()/5.0);
		this.window = w;
		
		this.filledDraw = filledDraw;
		round = new RoundRectangle2D.Float(x,y,width,height,fok1,fok2);/*lekerekített 
		sarkú négyszög,az utolsó két paraméter a behajlási fok milyen ívben hajoljon!!!*/
		
	}
	
	public TextField2D(String text,Font font,int x,int y,int width,Color cursorColor,Color textFieldColor,Color textColor
			,Color kijelolColor,Canvas canvas,boolean filledDraw,int fok1,int fok2) {
		sb = new StringBuilder();
		xcur = ycur = oldx = oldy = 0;/*belekattintáshoz van*/
		
		x1 = x2 = y1 = y2 = 0;/*kijelöléshez van*/
		oldx1 = oldx2 = oldy1 = oldy2 = 0;
		
		sajatSorszam = ownSorszam;
		ownSorszam++;
		
		sb.append(text);
		this.font = font;
		this.x = x;
		this.y = y;
		this.width = width;
		this.cursorColor = cursorColor;
		this.textFieldColor = textFieldColor;
		this.textColor = textColor;
		this.kijelolColor = kijelolColor;
		this.height = font.getSize() + (int)(font.getSize()/5.0);
		this.canvas = canvas;
		
		this.filledDraw = filledDraw;
		round = new RoundRectangle2D.Float(x,y,width,height,fok1,fok2);/*lekerekített 
		sarkú négyszög,az utolsó két paraméter a behajlási fok milyen ívben hajoljon!!!*/
		
	}
	
	public void mouseReleased(MouseEvent e) {
		oldx1 = x1;			// uj kijelolesbe kezdunk, igy az elozo
		oldy1 = y1;                     // kijeloles helyet a torles miatt megjegyezzuk
		oldx2 = x2;			
		oldy2 = y2;                  

		x2 = e.getX () - x;
		y2 = e.getY ();
		/*if(canvas != null){
			canvas.repaint();
		}else{
			window.repaint ();
		}*/
		
	}
	
	public void mousePressed(MouseEvent e) {
		kijeloles = true;
		
		int xx = e.getX();
		int yy = e.getY();
		
		if(xx >= x && xx <= x+width && yy >= y && yy <= y+height){
			cursorBirtokos = sajatSorszam;
		}
		
		oldx1 = x1;			// uj kijelolesbe kezdunk, igy az elozo
		oldy1 = y1;                     // kijeloles helyet a torles miatt megjegyezzuk
		x1 = e.getX () - x;
		y1 = e.getY ();	

		x2 = x1;			// az uj kijeloles me'g csak most kezdodik; ez
		y2 = y1;			// ures kijelolest jelent, azaz: (x1, y1) = (x2, y2)
		/*if(canvas != null){
			canvas.repaint();
		}else{
			window.repaint ();
		}*/
	}
	
	
	
	public void mouseClicked(MouseEvent e) {
		kijeloles = false;
		actionType = ActionType.CURSORMOVE;
		
		int xx = e.getX();
		int yy = e.getY();
		
		if(xx >= x && xx <= x+width && yy >= y && yy <= y+height){
			cursorBirtokos = sajatSorszam;
		}
		oldx = xcur;
		oldy = ycur;

		xcur = e.getX () - x;
		ycur = e.getY ();
		
		/*if(canvas != null){
			canvas.repaint();
		}else{
			window.repaint ();
		}*/
	}
	
	
	
	public void mouseDragged(MouseEvent e) {
		oldx1 = x1;			// uj kijelolesbe kezdunk, igy az elozo
		oldy1 = y1;                     // kijeloles helyet a torles miatt megjegyezzuk
		oldx2 = x2;			
		oldy2 = y2;                     

		x2 = e.getX () - x;
		y2 = e.getY ();
		/*if(canvas != null){
			canvas.repaint();
		}else{
			window.repaint ();
		}*/
	}
	
	

	
	
	public void keyPressed(KeyEvent e) {
		if(sajatSorszam == cursorBirtokos){
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				if(modifySzovegIndex > 0 && deletedSzovegIndex > 0){
					kijeloles = false;
					actionType = ActionType.CURSORMOVEWITHARROW;
					deletedSzovegIndex--;
					modifySzovegIndex--;
					/*if(canvas != null){
						canvas.repaint();
					}else{
						window.repaint ();
					}*/
				}
				
			}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				if(modifySzovegIndex < sb.length() && deletedSzovegIndex < sb.length()){
					kijeloles = false;
					actionType = ActionType.CURSORMOVEWITHARROW;
					deletedSzovegIndex++;
					modifySzovegIndex++;
					/*if(canvas != null){
						canvas.repaint();
					}else{
						window.repaint ();
					}*/
				}
				
			}else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				if(kijeloles){
					if(elsoTalalat < masodikTalalat){

						String eleje = sb.toString().substring(0,elsoTalalat);
						String vege = sb.toString().substring(masodikTalalat, sb.length());
						
						
						if(eleje.length() + vege.length() > 0){
							
							sb.delete(0,sb.length());
							sb.append(eleje);
							sb.append(vege);
						}else{
							sb.delete(0,sb.length());
							sb.append(" ");
						}
						
						deletedSzovegIndex = elsoTalalat;
						modifySzovegIndex = elsoTalalat;
						
						x1 = x2 = y1 = y2 = 0;/*kijelöléshez van*/
						oldx1 = oldx2 = oldy1 = oldy2 = 0;
						
						/*if(canvas != null){
							canvas.repaint();
						}else{
							window.repaint ();
						}*/
						kijeloles = false;
					}else{
						String eleje = sb.toString().substring(0,masodikTalalat);
						String vege = sb.toString().substring(elsoTalalat, sb.length());
						
						if(eleje.length() + vege.length() > 0){
							
							sb.delete(0,sb.length());
							sb.append(eleje);
							sb.append(vege);
						}else{
							sb.delete(0,sb.length());
							sb.append(" ");
						}
						
						deletedSzovegIndex = masodikTalalat;
						modifySzovegIndex = masodikTalalat;
						
						x1 = x2 = y1 = y2 = 0;/*kijelöléshez van*/
						oldx1 = oldx2 = oldy1 = oldy2 = 0;
						
						/*if(canvas != null){
							canvas.repaint();
						}else{
							window.repaint ();
						}*/
						kijeloles = false;
					}
				
				}else{
					if(deletedSzovegIndex > 1){
						actionType = ActionType.DELETE;
						//TextHitInfo talalat = megjelenito.hitTestChar (x,y);/*a szovegindexbe az x,y koordinátához tartozó elem indexét adja*/
						int szovegIndex = deletedSzovegIndex;
						String eleje = sb.toString().substring(0,szovegIndex-1);
						String vege = sb.toString().substring(szovegIndex, sb.length());
						sb.delete(0, sb.length());
						
						sb.append(eleje);
						sb.append(vege);
						
						oldx = xcur;
						oldy = ycur;
						deletedSzovegIndex --;
						
						/*if(canvas != null){
							canvas.repaint();
						}else{
							window.repaint ();
						}*/
					}else if(deletedSzovegIndex == 1 && sb.length() == 1){
						actionType = ActionType.DELETE;
						//TextHitInfo talalat = megjelenito.hitTestChar (x,y);/*a szovegindexbe az x,y koordinátához tartozó elem indexét adja*/
						int szovegIndex = deletedSzovegIndex;
						
						sb.delete(0, sb.length());
						
						sb.append(" ");
						
						oldx = xcur;
						oldy = ycur;
						deletedSzovegIndex--;
						
						/*if(canvas != null){
							canvas.repaint();
						}else{
							window.repaint ();
						}*/
					}else if(deletedSzovegIndex == 1 && sb.length() > 1){
						actionType = ActionType.DELETE;
						String szov = sb.toString().substring(1, sb.length());
						sb.delete(0, sb.length());
						sb.append(szov);
						
						deletedSzovegIndex--;
						
						/*if(canvas != null){
							canvas.repaint();
						}else{
							window.repaint ();
						}*/
						
					}
				}
			}else{
				/*beírás valami betût és nem a törlõ gomb*/
				if(kijeloles){
					if(elsoTalalat < masodikTalalat){
						char c = e.getKeyChar();
						String eleje = sb.toString().substring(0,elsoTalalat);
						String vege = sb.toString().substring(masodikTalalat, sb.length());
						sb.delete(0,sb.length());
						
						sb.append(eleje);
						sb.append(c);
						sb.append(vege);
						
						deletedSzovegIndex = elsoTalalat+1;
						modifySzovegIndex = elsoTalalat+1;
						
						x1 = x2 = y1 = y2 = 0;/*kijelöléshez van*/
						oldx1 = oldx2 = oldy1 = oldy2 = 0;
						
						/*if(canvas != null){
							canvas.repaint();
						}else{
							window.repaint();
						}*/
						kijeloles = false;
			
					}else{
						char c = e.getKeyChar();
						String eleje = sb.toString().substring(0,masodikTalalat);
						String vege = sb.toString().substring(elsoTalalat, sb.length());
						sb.delete(0,sb.length());
						
						sb.append(eleje);
						sb.append(c);
						sb.append(vege);
						
						deletedSzovegIndex = masodikTalalat+1;
						modifySzovegIndex = masodikTalalat+1;
						
						x1 = x2 = y1 = y2 = 0;/*kijelöléshez van*/
						oldx1 = oldx2 = oldy1 = oldy2 = 0;
						
						/*if(canvas != null){
							canvas.repaint();
						}else{
							window.repaint ();
						}*/
						kijeloles = false;
					}
				}else{
					if(megjelenito.getBounds().getWidth() + font.getSize() <  width){
						actionType = ActionType.MODIFY;
						
						if(sb.toString().equals(" ")){
							char c = e.getKeyChar();
							sb.delete(0, sb.length());
							sb.append(c);
							modifySzovegIndex = 1;
						}else{
							int szovegIndex = modifySzovegIndex;
							
							String eleje = sb.toString().substring(0, szovegIndex);
							String vege = sb.toString().substring(szovegIndex, sb.length());
							sb.delete(0, sb.length());
							
							char c = e.getKeyChar();
							
							sb.append(eleje);
							sb.append(c);
							sb.append(vege);
							
							modifySzovegIndex++;
						}
						
						/*if(canvas != null){
							canvas.repaint();
						}else{
							window.repaint ();
						}*/
					}
				}
			}
		}
	}

	
	public void draw(Graphics g){
		
		g2 = (Graphics2D) g;
		g2.clearRect(x, y, width, height);
		
		FontRenderContext frc = g2.getFontRenderContext ();
		Font betutipus = new Font (font.getName(), font.getStyle(), font.getSize());
		String szoveg = new String (sb.toString());
		megjelenito = new TextLayout (szoveg, betutipus, frc);
	
		if(this.szinAtmenetes){
			g2.setPaint(this.gp);
			if(round != null){
				if(filledDraw){
					
					g2.fill(round);
				}else{
					
					g2.draw(round);
				}
			}else{
				if(filledDraw){
					g2.setColor(textFieldColor);
					g2.fillRect(x, y, width, height);
				}else{
					g2.setColor(textFieldColor);
					g2.drawRect(x, y, width, height);
				}
			}
		}else{
			if(round != null){
				if(filledDraw){
					g2.setColor(textFieldColor);
					g2.fill(round);
				}else{
					g2.setColor(textFieldColor);
					g2.draw(round);
				}
			}else{
				if(filledDraw){
					g2.setColor(textFieldColor);
					g2.fillRect(x, y, width, height);
				}else{
					g2.setColor(textFieldColor);
					g2.drawRect(x, y, width, height);
				}
			}
		}
		
		if(kijeloles){
			
			if(sajatSorszam == cursorBirtokos){
				// az elozo kijeloles helyet kerdezzuk le
				TextHitInfo talalat = megjelenito.hitTestChar (oldx1, oldy1);
				elsoTalalat = talalat.getInsertionIndex ();

				talalat = megjelenito.hitTestChar (oldx2, oldy2);
				masodikTalalat = talalat.getInsertionIndex ();

				// az elozo kijelolest toroljuk (feherrel atrajzoljuk azt a reszt)
				Shape kijeloltTerulet = megjelenito.getLogicalHighlightShape (elsoTalalat, masodikTalalat);
				if(filledDraw){
					g2.setColor (this.textFieldColor);
				}else{
					g2.setColor (Color.white);
				}
				
				g2.translate (x, y+font.getSize());
				g2.fill (kijeloltTerulet);
				g2.translate (-x, -y-font.getSize());

				// az uj kijeleles helyet lekerdezzuk
				talalat = megjelenito.hitTestChar (x1, y1);
				elsoTalalat = talalat.getInsertionIndex ();

				talalat = megjelenito.hitTestChar (x2, y2);
				masodikTalalat = talalat.getInsertionIndex ();

				// az uj kijelolest pirossal kirajzoljuk
				kijeloltTerulet = megjelenito.getLogicalHighlightShape (elsoTalalat, masodikTalalat);
				g2.setColor (this.kijelolColor);
				g2.translate (x, y+font.getSize());
				g2.fill (kijeloltTerulet);
				g2.translate (-x, -y-font.getSize());
				/*System.out.println("kezdopozíció " + elsoTalalat);
				System.out.println("Végpozíció " + masodikTalalat);*/
			}
			
		}else{
			if(sajatSorszam == cursorBirtokos){
				
				// a kurzor regi poziciojat kerjuk le	
				TextHitInfo talalat = megjelenito.hitTestChar (oldx, oldy);
				int szovegIndex = talalat.getInsertionIndex ();
				
				// a regi kurzort toroljuk (feher szinnel rajzoljuk ki)
		                Shape[] kurzorok = megjelenito.getCaretShapes (szovegIndex);
		        if(filledDraw){
		        	g2.setColor (textFieldColor);
		        }else{
		        	g2.setColor (Color.white);
		        }
				
				g2.translate (x, y + font.getSize());
				g2.draw (kurzorok[0]);
				g2.translate (-x, -y - font.getSize());

				if (kurzorok[1] != null) {
					g2.setColor (Color.white);
					g2.translate (x, y + font.getSize());
					g2.draw (kurzorok[1]);
					g2.translate (-x, -y - font.getSize());
				}

				if(actionType == ActionType.CURSORMOVE){
					
					// a kurzor uj poziciojat kerjuk le	
					talalat = megjelenito.hitTestChar (xcur, ycur);
					szovegIndex = talalat.getInsertionIndex ();
					deletedSzovegIndex = szovegIndex;
					modifySzovegIndex = szovegIndex;
				}else if(actionType == ActionType.DELETE){
					szovegIndex = deletedSzovegIndex;
					modifySzovegIndex = deletedSzovegIndex;
				}else if(actionType == ActionType.MODIFY){
					szovegIndex = modifySzovegIndex;
					deletedSzovegIndex = modifySzovegIndex;
				}else if(actionType == ActionType.CURSORMOVEWITHARROW){
					szovegIndex = modifySzovegIndex;
					deletedSzovegIndex = modifySzovegIndex;
				}
				
					// a kurzort az uj helyen megjelenitjuk
	                kurzorok = megjelenito.getCaretShapes (szovegIndex);
					g2.setColor (cursorColor);
					g2.translate (x, y + font.getSize());
					g2.draw (kurzorok[0]);
					g2.translate (-x, -y - font.getSize());
				
				if (kurzorok[1] != null) {
					g2.setColor (Color.green);
					g2.translate (x, y + font.getSize());
					g2.draw (kurzorok[1]);
					g2.translate (-x, -y - font.getSize());
				}
			}
			/*Ezzel visszaállítom az alapértelmezett színre a rajzolást,esetleg,ha ezután rajzolna vki annak ne kelljen.*/
			g2.setColor(Color.black);
			
		}
		
		// megjelenitjuk a szoveget
		
			g2.setColor (textColor);
			megjelenito.draw (g2, x, y + font.getSize()); 
	}
	
	public String getText(){
		if(sb.toString().equals(" ")){
			return "";
		}
		return sb.toString();
	}
	
	public void setText(String text){
		sb.delete(0, sb.length());
		sb.append(text);
	}
	
	public boolean isCursorOwnerThis(){
		if(sajatSorszam == cursorBirtokos){
			return true;
		}else{
			return false;
		}
	}
	
	public static void lostTheFocus(){
		cursorBirtokos+=1000;
	}
	
	public void gainTheFocus(){
		cursorBirtokos = this.sajatSorszam;
	}
	
	public void setGradientPaint(GradientPaint gp){
		this.gp = gp;
		this.szinAtmenetes = true;
	}
	
	public void setGradientPaintToFalse(){
		this.szinAtmenetes = false;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public int getOldx1() {
		return oldx1;
	}

	public void setOldx1(int oldx1) {
		this.oldx1 = oldx1;
	}

	public int getOldx2() {
		return oldx2;
	}

	public void setOldx2(int oldx2) {
		this.oldx2 = oldx2;
	}

	public int getOldy1() {
		return oldy1;
	}

	public void setOldy1(int oldy1) {
		this.oldy1 = oldy1;
	}

	public int getOldy2() {
		return oldy2;
	}

	public void setOldy2(int oldy2) {
		this.oldy2 = oldy2;
	}

	public int getXcur() {
		return xcur;
	}

	public void setXcur(int xcur) {
		this.xcur = xcur;
	}

	public int getYcur() {
		return ycur;
	}

	public void setYcur(int ycur) {
		this.ycur = ycur;
	}

	public int getOldx() {
		return oldx;
	}

	public void setOldx(int oldx) {
		this.oldx = oldx;
	}

	public int getOldy() {
		return oldy;
	}

	public void setOldy(int oldy) {
		this.oldy = oldy;
	}

	public StringBuilder getSb() {
		return sb;
	}

	public void setSb(StringBuilder sb) {
		this.sb = sb;
	}

	public TextLayout getMegjelenito() {
		return megjelenito;
	}

	public void setMegjelenito(TextLayout megjelenito) {
		this.megjelenito = megjelenito;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public int getDeletedSzovegIndex() {
		return deletedSzovegIndex;
	}

	public void setDeletedSzovegIndex(int deletedSzovegIndex) {
		this.deletedSzovegIndex = deletedSzovegIndex;
	}

	public int getModifySzovegIndex() {
		return modifySzovegIndex;
	}

	public void setModifySzovegIndex(int modifySzovegIndex) {
		this.modifySzovegIndex = modifySzovegIndex;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getCursorColor() {
		return cursorColor;
	}

	public void setCursorColor(Color cursorColor) {
		this.cursorColor = cursorColor;
	}

	public Color getTextFieldColor() {
		return textFieldColor;
	}

	public void setTextFieldColor(Color textFieldColor) {
		this.textFieldColor = textFieldColor;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Color getKijelolColor() {
		return kijelolColor;
	}

	public void setKijelolColor(Color kijelolColor) {
		this.kijelolColor = kijelolColor;
	}

	public boolean isOwnCursor() {
		return ownCursor;
	}

	public void setOwnCursor(boolean ownCursor) {
		this.ownCursor = ownCursor;
	}

	public static int getOwnSorszam() {
		return ownSorszam;
	}

	public static void setOwnSorszam(int ownSorszam) {
		TextField2D.ownSorszam = ownSorszam;
	}

	public static int getCursorBirtokos() {
		return cursorBirtokos;
	}

	public static void setCursorBirtokos(int cursorBirtokos) {
		TextField2D.cursorBirtokos = cursorBirtokos;
	}

	public int getSajatSorszam() {
		return sajatSorszam;
	}

	public void setSajatSorszam(int sajatSorszam) {
		this.sajatSorszam = sajatSorszam;
	}

	public boolean isFilledDraw() {
		return filledDraw;
	}

	public void setFilledDraw(boolean filledDraw) {
		this.filledDraw = filledDraw;
	}

	public boolean isKijeloles() {
		return kijeloles;
	}

	public void setKijeloles(boolean kijeloles) {
		this.kijeloles = kijeloles;
	}

	public Container getWindow() {
		return window;
	}

	public void setWindow(Container window) {
		this.window = window;
	}

	public int getElsoTalalat() {
		return elsoTalalat;
	}

	public void setElsoTalalat(int elsoTalalat) {
		this.elsoTalalat = elsoTalalat;
	}

	public int getMasodikTalalat() {
		return masodikTalalat;
	}

	public void setMasodikTalalat(int masodikTalalat) {
		this.masodikTalalat = masodikTalalat;
	}

	public RoundRectangle2D getRound() {
		return round;
	}

	public void setRound(RoundRectangle2D round) {
		this.round = round;
	}

	public GradientPaint getGp() {
		return gp;
	}

	public void setGp(GradientPaint gp) {
		this.gp = gp;
	}

	public boolean isSzinAtmenetes() {
		return szinAtmenetes;
	}

	public void setSzinAtmenetes(boolean szinAtmenetes) {
		this.szinAtmenetes = szinAtmenetes;
	}
	
	
}
