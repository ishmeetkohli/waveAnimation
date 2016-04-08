package com.animation.waves.utils;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class RenderShapes {

	static ShapeRenderer shapeRenderer;
	
	static {
		shapeRenderer = new ShapeRenderer();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glLineWidth(2);
	}
	
	public static void drawLine(Vector3 vector1, Vector3 vector2, Color color) {
		shapeRenderer.setColor(color);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.line(vector1, vector2);
		shapeRenderer.end();
	}
	
	public static void drawPoints(Vector3 vector1, Color color) {
		shapeRenderer.setColor(color);
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.point(vector1.x, vector1.y, vector1.z);
		shapeRenderer.end();
	}
	
	public static void drawPoints(ArrayList<Vector3> points, Color color) {
		shapeRenderer.setColor(color);
		shapeRenderer.begin(ShapeType.Point);
		for (Vector3 point : points) {
			shapeRenderer.point(point.x, point.y, point.z);
		}
		shapeRenderer.end();
	}
	
	public static void setProjectionMatrix(PerspectiveCamera camera) {
		shapeRenderer.setProjectionMatrix(camera.combined);
	}
	
}
