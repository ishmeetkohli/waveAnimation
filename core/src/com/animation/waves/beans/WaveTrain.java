package com.animation.waves.beans;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class WaveTrain {

	public static final int RISING = 1;
	public static final int FLOWING = 2;
	public static final int FALLING = 3;
	
	public static final int TRANSITION_TIME = 3;

	public static final int MEDIAN_WAVE_LENGTH = 20;
	public static final float W_AMPLITUDE_RATIO = 0.5f;
	public static final int GRAVITY = 10;
	public static final int WIND_ANGLE = 90;
	public static final double SPEED = 10;

	Random random;

	Vector2 direction;
	double a;
	double waveLength;
	double speed;
	double angle;
	
	double w;
	double fy;
	
	float life;
	float time;
	float stateFactor;
	int state;
	
	public WaveTrain() {
		random = new Random();
		direction = new Vector2();
		initialize();
	}

	private void initialize() {
		time = 0;
		stateFactor = 0;
		state = RISING;
		
		waveLength = MEDIAN_WAVE_LENGTH/2 + random.nextInt(2 * MEDIAN_WAVE_LENGTH);
		w = Math.sqrt(GRAVITY * 2 * Math.PI / waveLength);
		
//		Qi x wi x Ai
//		a = 0.7 / w;
		a = W_AMPLITUDE_RATIO / w;
		fy = SPEED * 2 * Math.PI / waveLength;
		angle = WIND_ANGLE - 20 + random.nextInt(40);
		direction.set((float) Math.cos(angle * Math.PI / 180), (float) Math.sin(angle * Math.PI / 180));
		
		speed = 1;
		life = 5 + random.nextInt(10);
	}

	public double w() {
		return w;
	}
	
	public double fy() {
		return fy;
	}

	public double a() {
		return a * stateFactor/TRANSITION_TIME;
	}

	public Vector2 getDirection() {
		return direction;
	}
	
	public void update(float deltaTime) {

		switch (state) {
		case RISING:
			stateFactor += deltaTime;
			if (stateFactor > TRANSITION_TIME) {
				stateFactor = 3;
				state = FLOWING;
			}
			break;

		case FLOWING:
			time += deltaTime;
			if (time > life) {
				state = FALLING;
			}
			break;

		case FALLING:
			stateFactor -= deltaTime;
			if (stateFactor < 0) {
				initialize();
			}
			break;
		}
	}
	
}
