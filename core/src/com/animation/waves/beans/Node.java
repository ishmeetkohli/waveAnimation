package com.animation.waves.beans;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Node {

	public static final float RADIUS = 0.5f;

	Vector3 position;
	Vector3 normal;
	Vector2 initialPosition;
	float initialMagnitude;

	ModelInstance modelInstance;

	public Node(Vector3 postion) {
		this.position = postion;
		this.normal = new Vector3();
		initialPosition = new Vector2(postion.x, postion.y);
		initialMagnitude = initialPosition.len();
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getNormal() {
		return normal;
	}

	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}

	public void addToNormal(Vector3 normal) {
		this.normal.add(normal);
	}

	public Vector2 getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(Vector2 initialPosition) {
		this.initialPosition = initialPosition;
	}

	public float getInitialMagnitude() {
		return initialMagnitude;
	}

	public void setInitialMagnitude(float initialMagnitude) {
		this.initialMagnitude = initialMagnitude;
	}

	// public void render(ModelBatch modelBatch) {
	// this.modelInstance.transform.setToTranslation(position);
	// modelBatch.render(modelInstance);
	// }

	public void dispose() {
	}

	public void normalizeNormal() {
		this.normal = this.normal.nor();
	}

}
