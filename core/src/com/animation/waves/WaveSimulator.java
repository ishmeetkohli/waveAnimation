package com.animation.waves;

import com.animation.waves.logic.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class WaveSimulator extends ApplicationAdapter {

	public static final float DEPTH = 100;

	private PerspectiveCamera camera;
	ModelBatch modelBatch;
	ShapeRenderer shapeRenderer;
	World world;
	
	private FrameBuffer frameBuffer;
	SpriteBatch frameBufferBatch;
	FPSLogger fpsLogger;
	
	int screenWidth;
	int screenHeight;
	
	Vector3 target;

	@Override
	public void create() {
		world = new World();
		world.trainCount = World.TRAIN_COUNT;
		world.initializeWaveTrains();
		modelBatch = new ModelBatch();
		shapeRenderer = new ShapeRenderer();

		target = new Vector3(0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, 0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, 0);
		
		camera = new PerspectiveCamera(35, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
//		camera.position.set(0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, 0, 10);
//		camera.lookAt(0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, 0);

		camera.position.set(0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, 0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, DEPTH);
		camera.lookAt(0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, 0.5f * World.NODE_DENSITY * World.STRUCTURAL_REST_LENGTH, 0);
		
		
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
		
//		MyGestureListener gestureListener = new MyGestureListener(camera);
		
		CameraInputController inputController = new CameraInputController(camera);
		inputController.target.set(target);
		Gdx.input.setInputProcessor(inputController);
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		frameBuffer = new FrameBuffer(Format.RGB888, screenWidth, screenHeight, true);
		frameBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		frameBufferBatch = new SpriteBatch();
		fpsLogger = new FPSLogger();
	}

	@Override
	public void render() {
		update();
		fpsLogger.log();
		
		frameBuffer.begin();
		
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Gdx.gl20.glClearColor(0, 0, 0, 0);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
		
		modelBatch.begin(camera);
		modelBatch.getRenderContext().setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		world.render(modelBatch, camera);
		modelBatch.end();
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
		frameBuffer.end();
		 
	   frameBufferBatch.begin();
	   frameBufferBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, screenWidth, screenHeight, 0, 0, 1, 1);
	   frameBufferBatch.end();
	}

	public void update() {
		world.update(Gdx.graphics.getDeltaTime());
    	
		if (Gdx.input.isKeyPressed(Keys.EQUALS)) {
			camera.fieldOfView--;
		}

		if (Gdx.input.isKeyPressed(Keys.MINUS)) {
			camera.fieldOfView++;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
//			switch (world.state) {
//			case World.STATE_MESH:
//				world.state = World.STATE_ONE_WAVE;
//				world.trainCount = 1;
//				world.initializeWaveTrains();
//				break;
//			case World.STATE_ONE_WAVE:
//				world.state = World.STATE_MULTIPLE_WAVES;
//				world.trainCount = World.TRAIN_COUNT;
//				world.initializeWaveTrains();
//				break;
//			case World.STATE_MULTIPLE_WAVES:
//				world.state = World.STATE_SHADED;
//				break;
//			case World.STATE_SHADED:
//				world.state = World.STATE_ENIVRONMENT;
//				break;
//			}
			
			world.toggleIsShaded();
		}
		
		camera.update();
	}
}
