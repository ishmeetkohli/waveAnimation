package com.animation.waves.utils;

import com.animation.waves.logic.World;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.input.GestureDetector;

public class MyGestureDetector extends GestureDetector {
	
	World world;
	
	public MyGestureDetector(MyGestureListener gestureListener, World world) {
		super(gestureListener);
		this.world = world;
	}

	@Override
    public boolean keyDown(int keycode) {
        super.keyDown(keycode);
        
        switch(keycode){
        case Keys.SPACE:
        	world.toggleIsShaded();
        	break;
        }
        return false;
    }
	
}
