package com.animation.waves.utils;

import com.animation.waves.logic.World;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGestureListener implements GestureListener {
	PerspectiveCamera camera;
	
	public MyGestureListener(PerspectiveCamera camera) {
		this.camera = camera;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		camera.rotateAround(new Vector3(0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH,0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH,0), new Vector3(0,1,0), -deltaX);
		camera.rotateAround(new Vector3(0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH,0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH,0), new Vector3(1,0,0), -deltaY);
		return false;
	}


	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
