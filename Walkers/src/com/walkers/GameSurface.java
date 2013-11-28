package com.walkers;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

  GameEngine gameEngine;
  SurfaceHolder surfaceHolder;
  Context context;
  private String stage = "nada";
  private GameThread gameThread;
  private float lastTouchX;
  private float lastTouchY;
  private long touchDownTime = 0;
  private static final int INVALID_POINTER_ID = -1;
  private int activePointerId = INVALID_POINTER_ID;
 public void setStage(String stg){
	 stage = stg;
	 InitView();
 }
  public GameSurface(Context context, AttributeSet attrs, int defStyle) {
   super(context, attrs, defStyle);
   this.context = context;
   
   InitView();
  }
 
  public GameSurface(Context context, AttributeSet attrs) {
   super(context, attrs);
   this.context = context;
   InitView();
  }
 
  void InitView() {
		  SurfaceHolder surfaceHolder = getHolder();
		  surfaceHolder.addCallback(this);
		  gameEngine = new GameEngine();
		  gameEngine.Init(context, stage);
		  gameThread = new GameThread(surfaceHolder, context, new Handler(),
				  gameEngine);
		  setFocusable(true);
   
  }
 
  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
   boolean retry = true;
   gameThread.state = GameThread.PAUSED;
   while (retry) {
    try {
     gameThread.join();
     retry = false;
    } catch (InterruptedException e) {
    }
   }
  }
 
  @Override
  public void surfaceCreated(SurfaceHolder arg0) {
   if (gameThread.state == GameThread.PAUSED) {
    gameThread = new GameThread(getHolder(), context, new Handler(),
      gameEngine);
    gameThread.start();
   } else {
    gameThread.start();
   }
  }
 
  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
    int height) {
   gameEngine.setSurfaceDimensions(width, height);
  }
  @Override
  public boolean onTouchEvent(MotionEvent motionEvent) {
   final int action = motionEvent.getAction();

   switch (action & MotionEvent.ACTION_MASK) {

   case MotionEvent.ACTION_DOWN: {
    float x = motionEvent.getX();
    float y = motionEvent.getY();
    /*Log.d("Valor de x","x="+x);
    Log.d("Valor de y","y="+y);*/
    lastTouchX = x;
    lastTouchY = y;
    gameEngine.ult_x =(int) x;
    gameEngine.ult_y =(int) y;
    touchDownTime = System.currentTimeMillis();
    activePointerId = motionEvent.getPointerId(0);
    break;
   }

   case MotionEvent.ACTION_MOVE: {
	   final int pointerIndex = motionEvent
	     .findPointerIndex(activePointerId);
	   final float x = motionEvent.getX(pointerIndex);
	   final float y = motionEvent.getY(pointerIndex);
	   final float dX = x - lastTouchX;
	   final float dY = y - lastTouchY;

	   lastTouchX = x;
	   lastTouchY = y;

	   if (dY < -10) {
	    gameEngine.pendingEvent = GameEngine.EVENT_THRUST;
	    break;
	   }
	   if (dX > 10) {
	    gameEngine.pendingEvent = GameEngine.EVENT_RIGHT;
	    break;
	   }
	   if (dX < -10) {
	    gameEngine.pendingEvent = GameEngine.EVENT_LEFT;
	    break;
	   }
	   break;
	 }	

   case MotionEvent.ACTION_UP: {
    if (System.currentTimeMillis() < touchDownTime + 150) {
    	gameEngine.pendingEvent = GameEngine.EVENT_FIRE;
    }
    activePointerId = INVALID_POINTER_ID;
    break;
   }

   case MotionEvent.ACTION_CANCEL: {
    activePointerId = INVALID_POINTER_ID;
    break;
   }

   case MotionEvent.ACTION_POINTER_UP: {
    final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    final int pointerId = motionEvent.getPointerId(pointerIndex);
    if (pointerId == activePointerId) {
     final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
     lastTouchX = motionEvent.getX(newPointerIndex);
     lastTouchY = motionEvent.getY(newPointerIndex);
     activePointerId = motionEvent.getPointerId(newPointerIndex);
    }
    break;
   }
   }

   return true;

  }

}