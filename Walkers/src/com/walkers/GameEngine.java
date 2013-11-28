package com.walkers;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;

public class GameEngine {

	Context context;
	Resources resources;

	public float screenWidth;
	public float screenHeight;
	private Paint blackPaint;
	private Paint semiTransparentPaint;

	private Paint textPaint;
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private List<TempSprite> temps = new ArrayList<TempSprite>();
	private SharedPreferences sharedPref = null;
	public static final int EVENT_NONE = 0;
	public static final int EVENT_LEFT = 1;
	public static final int EVENT_RIGHT = 2;
	public static final int EVENT_THRUST = 3;
	public static final int EVENT_FIRE = 4;
	public int pendingEvent = EVENT_NONE;
	public int ult_x = 0;
	public int ult_y = 0;
	private int lvlActual = 1;
	private int contmalos = Opciones.getMalos();
	private float smallTextSize = 25;
	private float mediumTextSize = 30;
	private float largeTextSize = 80;
	private String stage = "nada";
	private int salvados = 0;
	private int ultTiempo = 0;
	private static final int WAITING_FOR_SURFACE = 0;
	private static final int COUNTDOWN = 1;
	private static final int RUNNING = 2;
	private static final int GAMEOVER = 3;
	private static final int DIBUJONIVELES = 4;
	public static final int TUTORIAL = 5;
	private int mode = WAITING_FOR_SURFACE;
	private Bitmap bmpBlood;

	public void Init(Context context, String stg) {
		stage = stg;

		textPaint = new Paint();
		textPaint.setColor(Color.CYAN);
		textPaint.setTextAlign(Paint.Align.CENTER);
		this.context = context;
		resources = context.getResources();
		sharedPref = context.getSharedPreferences("key_niveles",
				Context.MODE_PRIVATE);
		salvados = sharedPref.getInt("salvados", 0);
		ultTiempo = sharedPref.getInt("tiempo1", 0);
		blackPaint = new Paint();
		blackPaint.setColor(Color.BLACK);
		blackPaint.setStyle(Style.FILL);
		/*fgfcgfc
		 * SharedPreferences.Editor editor = sharedPref.edit();
		 * editor.putInt("nivel", lvlSiguiente); editor.commit();
		 */
		bmpBlood = BitmapFactory.decodeResource(resources, R.drawable.blood1);
		// New Paint for "shading effects"
		semiTransparentPaint = new Paint();
		semiTransparentPaint.setColor(Color.BLACK);
		semiTransparentPaint.setStyle(Style.FILL);
		semiTransparentPaint.setAlpha(200);

	}

	public void setSurfaceDimensions(int width, int height) {
		screenWidth = width;
		screenHeight = height;

		if (mode == WAITING_FOR_SURFACE) {

			Log.d("Juna", "Creados sprites");
			mode = COUNTDOWN;

			// setLevel(nivelconseguido);
			/*
			 * mode = 8;; Bitmap bmp = BitmapFactory.decodeResource(resources,
			 * R.drawable.bad4); sprite = new Sprite((int)screenHeight,
			 * (int)screenWidth, bmp);
			 * Log.d("Franco","Iniciado controlador de niveles con stage"
			 * +stage);
			 */

		}

	}

	public void update() {
		switch (mode) {
		case COUNTDOWN: {
			if (ult_y > screenHeight * 0.7) {
				createSprites();
				ult_y = 0;
				mode = RUNNING;
			}
			break;
		}
		case WAITING_FOR_SURFACE: {
			// Don't update anything. Just wait until setSurfaceDimensions() is
			// called.

			break;
		}

		case RUNNING: {
			// Standard mode - update all objects
			
			for (int i = sprites.size() - 1; i >= 0; i--) {
				Sprite sprite = sprites.get(i);

				if (sprite.isCollision(ult_x, ult_y)) {
					ult_x = 0;
					ult_y = 0;
					if (sprite.moral.equals("malo")) {
						contmalos--;
						Log.d("Juna","Conteo malo restado, ahora es "+contmalos);
					}
					sprites.remove(sprite);
					temps.add(new TempSprite(temps, this, sprite.x, sprite.y,
							bmpBlood));
					break;
				}
				for (int subi = sprites.size() - 1; subi >= 0; subi--) {
					Sprite subsprite = sprites.get(subi);
					if (subsprite != sprite) {
						if (sprite.moral.equals("bueno")
								&& subsprite.moral.equals("malo")) {
							if (sprite.isCollision(subsprite.x, subsprite.y)) {
								ult_x = 0;
								ult_y = 0;
								sprites.remove(sprite);
								temps.add(new TempSprite(temps, this, sprite.x,
										sprite.y, bmpBlood));
								break;
							}
						}
					}
				}
			}// fcghcvf
			int contbuenos = 0;
			for (int subi = sprites.size() - 1; subi >= 0; subi--) {
				Sprite sprite = sprites.get(subi);
				if (sprite.moral.equals("bueno")) {
					contbuenos++;
				}
			}
//			if (contbuenos == 0) {
//				mode = GAMEOVER;
//			}
			if (contmalos == 0) {
				Log.d("Juna","Conteo malo es 0");
				
				if(salvados < contbuenos){
					salvados = contbuenos;
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putInt("salvados", contbuenos);
					editor.commit();
				}

				ult_x = 0;
				ult_y = 0;
				contmalos = Opciones.getMalos();
				mode = COUNTDOWN;
			}
			break;
		}

		case GAMEOVER: {
			// Nothing needed here

			break;
		}

		}

	}

	public void draw(Canvas canvas) {
		switch (mode) {
		case COUNTDOWN: {
			canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
					blackPaint);
			canvas.drawText("La mayor salvacion fue: " + salvados, screenWidth / 2,
					largeTextSize, textPaint);
			canvas.drawText("Reiniciar", screenWidth / 2,
					(int)(screenHeight * 0.8), textPaint);
		}
		case WAITING_FOR_SURFACE: {
			// Don't draw anything before the surface is ready
			break;
		}

		case RUNNING: {
			canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
					blackPaint);
			for (int i = temps.size() - 1; i >= 0; i--) {
				temps.get(i).draw(canvas);
			}
			for (Sprite sprite : sprites) {
				sprite.draw(canvas);
			}

			break;
		}

		case GAMEOVER: {
			break;
		}

		}

	}

	private void createSprites() {
		for(int i=0;i<Opciones.getMalos();i++){
			switch(i){
			case 0:{
				sprites.add(createSprite(R.drawable.bad1, "malo"));
				break;
			}
			case 1:{
				sprites.add(createSprite(R.drawable.bad2, "malo"));
				break;
			}
			case 2:{
				sprites.add(createSprite(R.drawable.bad3, "malo"));
				break;
			}
			case 3:{
				sprites.add(createSprite(R.drawable.bad4, "malo"));
				break;
			}
			case 4:{
				
				sprites.add(createSprite(R.drawable.bad5, "malo"));
				break;
			}
			case 5:{
				sprites.add(createSprite(R.drawable.bad6, "malo"));
				break;
			}
			case 6:{
				sprites.add(createSprite(R.drawable.bad1, "malo"));
				break;
			}
			
			case 7:{
				sprites.add(createSprite(R.drawable.bad2, "malo"));
				break;
				
			}
			case 8:{
				sprites.add(createSprite(R.drawable.bad3, "malo"));
				break;
			}
			case 9:{
				sprites.add(createSprite(R.drawable.bad4, "malo"));
				break;
			}
			}
			
		}

		sprites.add(createSprite(R.drawable.good1, "bueno"));
		sprites.add(createSprite(R.drawable.good2, "bueno"));
		sprites.add(createSprite(R.drawable.good3, "bueno"));
		sprites.add(createSprite(R.drawable.good4, "bueno"));
		sprites.add(createSprite(R.drawable.good5, "bueno"));
		sprites.add(createSprite(R.drawable.good6, "bueno"));
	}

	private Sprite createSprite(int resouce, String moral) {
		Bitmap bmp = BitmapFactory.decodeResource(resources, resouce);
		return new Sprite(this, bmp, moral);
	}

	public float getWidth() {
		return screenWidth;
	}

	public float getHeight() {
		return screenHeight;
	}
}
