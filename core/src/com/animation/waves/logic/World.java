package com.animation.waves.logic;

import java.util.ArrayList;
import java.util.Random;

import com.animation.waves.beans.Node;
import com.animation.waves.beans.WaveTrain;
import com.animation.waves.utils.EnvironmentCubemap;
import com.animation.waves.utils.TestShader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class World {

	public static final int STATE_MESH = 0;
	public static final int STATE_ONE_WAVE = 1;
	public static final int STATE_MULTIPLE_WAVES = 2;
	public static final int STATE_SHADED = 3;
	public static final int STATE_ENIVRONMENT = 4;
	
	public static final float STRUCTURAL_REST_LENGTH = 1;
	public static final int NODE_DENSITY = 64;
	public static final int TRAIN_COUNT = 10;

	Texture texture;
	Node[][] nodes;
	
	ArrayList<WaveTrain> waveTrains; 
	private Mesh mesh;
	
	Boolean isShaded = false;
	
	Model model;
	ModelInstance modelInstance;
	ModelBuilder modelBuilder;
	MeshPartBuilder meshBuilder;
	
	public Environment environment;
	EnvironmentCubemap environmentCubeMap;
	
	Shader shader;
	Random random;
	
	public int state;
	public int trainCount;
	
	public Node[][] getNodes() {
		return nodes;
	}

	public void toggleIsShaded() {
		this.isShaded = !this.isShaded;
	}

	public World() {
		random = new Random();
		nodes = new Node[NODE_DENSITY][NODE_DENSITY];

		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[0].length; j++) {
				nodes[i][j] = new Node(new Vector3(j * STRUCTURAL_REST_LENGTH, i * STRUCTURAL_REST_LENGTH, 0));
			}
		}
		
		updateNodeNormals();
		
		waveTrains = new ArrayList<WaveTrain>();
		
//		meshBuilder = new MeshBuilder();
//		mesh = createMesh();
		texture = new Texture("ocean.jpg");
		
		modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		createMesh(modelBuilder);
//		modelBuilder.part("Mesh 001", mesh, GL20.GL_TRIANGLES, new Material());
		model = modelBuilder.end();
		
		modelInstance = new ModelInstance(model);
        environmentCubeMap = new EnvironmentCubemap(new Pixmap(Gdx.files.internal("skyMapSmall.png")));
//        environmentCubeMap = new EnvironmentCubemap(new Pixmap(Gdx.files.internal("skybox_texture.jpg")));

//		environment = new Environment();
//		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
//		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		shader = new TestShader();
		shader.init();
		
		state = STATE_SHADED;
	}
	
	float time = 0;

	public void resetNodePositions(float deltaTime) {
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[0].length; j++) {
					nodes[i][j].getPosition().x = nodes[i][j].getInitialPosition().x;
					nodes[i][j].getPosition().y = nodes[i][j].getInitialPosition().y;
					nodes[i][j].getPosition().z = 0;
				}
			}
	}
	
	public void initializeWaveTrains(){
		for (int i = 0; i < trainCount; i++) {
			waveTrains.add(new WaveTrain());
		}
	}
	
	Vector2 positionFactor = new Vector2();
	float scalingFactor;
	public void updateNodePositions(float deltaTime) {
		resetNodePositions(deltaTime);
		for (WaveTrain waveTrain : waveTrains) {
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[0].length; j++) {
					scalingFactor =  (float) (waveTrain.a() * Math.cos(waveTrain.fy() * time + waveTrain.w() * waveTrain.getDirection().dot(nodes[i][j].getInitialPosition())));
					positionFactor.set(waveTrain.getDirection());
					positionFactor.nor().scl(scalingFactor);
					
					nodes[i][j].getPosition().x += positionFactor.x;
					nodes[i][j].getPosition().x += positionFactor.y;
					nodes[i][j].getPosition().z += (float) (waveTrain.a() * Math.sin(waveTrain.fy() * time + waveTrain.w() * waveTrain.getDirection().dot(nodes[i][j].getInitialPosition())));
				}
			}
			
			waveTrain.update(deltaTime);
		}
		
		time += deltaTime;
	}
	
	public void resetNodeNormals() {
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[0].length; j++) {
				nodes[i][j].getNormal().x = 0;
				nodes[i][j].getNormal().y = 0;
				nodes[i][j].getNormal().z = 0;
			}
		}
}
	
	public void updateNodeNormals() {
		resetNodeNormals();
			for (int i = 0; i < nodes.length - 1; i++) {
				for (int j = 0; j < nodes[0].length - 1; j++) {
					addToNormals(nodes[i][j],nodes[i][j+1],nodes[i+1][j]);
					addToNormals(nodes[i][j+1],nodes[i+1][j+1],nodes[i+1][j]);
				}
			}
			
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[0].length; j++) {
					nodes[i][j].normalizeNormal();
				}
			}
		}

	ArrayList<Float> vertexList = new ArrayList<Float>();
	float[] vertexArray = new float[(3 + 3) * 4 * (NODE_DENSITY - 1) * (NODE_DENSITY - 1)];
	
	VertexInfo v1 = new VertexInfo();
	VertexInfo v2 = new VertexInfo();
	VertexInfo v3 = new VertexInfo();
	VertexInfo v4 = new VertexInfo();

	short index1, index2, index3, index4;
	
	private void createMesh(ModelBuilder modelBuilder2) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				modelBuilder.node().id = "part" + i + j;
				meshBuilder = modelBuilder.part("part" + i + j, GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material());
				
				for (int row = i * NODE_DENSITY / 2; row < (i + 1) * NODE_DENSITY / 2; row++) {
					for (int column = j * NODE_DENSITY / 2; column < (j + 1) * NODE_DENSITY / 2; column++) {
						if (row == NODE_DENSITY - 1 || column == NODE_DENSITY - 1) {
							continue;
						}

						// Top Left Vertex
						v1.setPos(nodes[row][column].getPosition());
						v1.setNor(nodes[row][column].getNormal());
						index1 = meshBuilder.vertex(v1);

						// Top Right Vertex
						v2.setPos(nodes[row][column + 1].getPosition());
						v2.setNor(nodes[row][column + 1].getNormal());
						index2 = meshBuilder.vertex(v2);

						// Bottom Left Vertex
						v3.setPos(nodes[row + 1][column].getPosition());
						v3.setNor(nodes[row + 1][column].getNormal());
						index3 = meshBuilder.vertex(v3);

						// Bottom Right Vertex
						v4.setPos(nodes[row + 1][column + 1].getPosition());
						v4.setNor(nodes[row + 1][column + 1].getNormal());
						index4 = meshBuilder.vertex(v4);

						meshBuilder.triangle(index1, index2, index3);
						meshBuilder.triangle(index2, index4, index3);
					}
				}

			}
		}
//		return meshBuilder.end();
	}
	
	Vector3 u = new Vector3();
	Vector3 v = new Vector3();
	
	private void addToNormals(Node node1, Node node2, Node node3) {
		u.set(node1.getPosition());
		v.set(node2.getPosition());
		
		v.sub(node3.getPosition());
		
		u.crs(v);
		
		node1.addToNormal(u);
		node2.addToNormal(u);
		node3.addToNormal(u);
	}

	int index = 0;
	private void updateMesh() {
		vertexList.clear();

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				
				for (int row = i * NODE_DENSITY / 2; row < (i + 1) * NODE_DENSITY / 2; row++) {
					for (int column = j * NODE_DENSITY / 2; column < (j + 1) * NODE_DENSITY / 2; column++) {
						if (row == NODE_DENSITY - 1 || column == NODE_DENSITY - 1) {
							continue;
						}
						
						// Top Left Vertex
						setVertices(vertexList, nodes[row][column]);

						// Top Right Vertex
						setVertices(vertexList, nodes[row][column + 1]);

						// Bottom Left Vertex
						setVertices(vertexList, nodes[row + 1][column]);

						// Bottom Right Vertex
						setVertices(vertexList, nodes[row + 1][column + 1]);
					}
				}
				
				index = 0;
				for (Float f : vertexList) {
					vertexArray[index++] = f;
				}
				
				mesh = model.getNode("part" + i +j).parts.get(0).meshPart.mesh;
				mesh.updateVertices(0, vertexArray);
			}
		}
	}

	private void setVertices(ArrayList<Float> vertexList, Node node) {
		vertexList.add(node.getPosition().x);
		vertexList.add(node.getPosition().y);
		vertexList.add(node.getPosition().z);
		
		vertexList.add(node.getNormal().x);
		vertexList.add(node.getNormal().y);
		vertexList.add(node.getNormal().z);
	}

	public void update(float deltaTime) {
		switch(state) {
		case STATE_ONE_WAVE:
		case STATE_MULTIPLE_WAVES:
		case STATE_SHADED:
		case STATE_ENIVRONMENT:
			updateNodePositions(deltaTime);
			updateNodeNormals();
			updateMesh();
			break;
		}
	}
	
	public void render(ModelBatch modelBatch, PerspectiveCamera camera) {
//		switch(state) {
//		case STATE_MESH :
//		case STATE_ONE_WAVE :
//		case STATE_MULTIPLE_WAVES :
//			RenderShapes.setProjectionMatrix(camera);
//			for (int i = 0; i < nodes.length; i++) {
//				for (int j = 0; j < nodes[0].length; j++) {
//					RenderShapes.drawPoints(nodes[i][j].getPosition(), Color.WHITE);
//				}
//			}
//			break;
//		case STATE_SHADED:
//			modelBatch.render(modelInstance, shader);
//			break;
//		case STATE_ENIVRONMENT:
//			environmentCubeMap.render(camera);
//			modelBatch.render(modelInstance, shader);
//			break;
//		}
		
		modelBatch.render(modelInstance, shader);
		
		if(isShaded) {
			environmentCubeMap.render(camera);
		}
	}
}
