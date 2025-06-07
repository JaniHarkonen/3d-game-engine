package gameengine.game;

import org.joml.Vector3f;
import org.lwjgl.assimp.Assimp;

import gameengine.engine.Engine;
import gameengine.engine.IScene;
import gameengine.engine.ITickable;
import gameengine.engine.asset.Animation;
import gameengine.engine.asset.AssetManager;
import gameengine.engine.asset.Mesh;
import gameengine.engine.asset.Texture;
import gameengine.engine.physics.IPhysicsObject;
import gameengine.engine.renderer.component.Material;
import gameengine.engine.window.Window;
import gameengine.game.component.Model;
import gameengine.game.component.light.PointLight;
import gameengine.logger.Logger;
import gameengine.test.TestCamera;
import gameengine.test.TestModel;
import gameengine.test.TestPlane;
import gameengine.test.TestPlayer;
import gameengine.util.FileUtils;

public class Game implements ITickable {

	private AssetManager assetManager;
	private Scene worldScene;
	
	public Game() {
		this.assetManager = new AssetManager();
		this.worldScene = null;
	}
	
	
	public void setup() {
		this.preloadAssets();
		this.worldScene = new Scene();
		this.worldScene.addObject(new TestCamera());
		
		PointLight testPointLight = new PointLight(new Vector3f(1, 1, 1), 1.0f, 0);
		testPointLight.getTransform().setPosition(13, 1f, 0);
		this.worldScene.addObject(testPointLight);
		
		testPointLight = new PointLight(new Vector3f(1, 1, 1), 0.5f, 1);
		testPointLight.getTransform().setPosition(0, 1, 5);
		this.worldScene.addObject(testPointLight);
		
		AssetManager.Group assets = this.getAssets();
		
			// Outside scene
		Model model = new Model((Mesh) assets.get("mesh-road-test"));
		model.setMaterial(Material.create((Texture) assets.get("tex-road-test")));
		/*Model model = new Model((Mesh) assets.get("mesh-outside"));
        model.setMaterial(
    		Material.create((Texture) assets.get("tex-pavement1")),
    		Material.create((Texture) assets.get("tex-concrete-block-1")),
    		Material.create((Texture) assets.get("tex-metal-dirtyrust")),
    		Material.create((Texture) assets.get("tex-concrete-wall-1")),
    		Material.create((Texture) assets.get("tex-concrete-barrier-dirty")),
    		Material.create((Texture) assets.get("tex-lampbox-metal")),
    		Material.create((Texture) assets.get("tex-metal-door1")),
    		Material.create((Texture) assets.get("tex-concrete-wall-2")),
    		Material.create((Texture) assets.get("tex-metal-skirting")),
    		Material.create((Texture) assets.get("tex-window-blinds1")),
    		Material.create((Texture) assets.get("tex-wood-planks1")),
    		Material.create((Texture) assets.get("tex-concrete-barrier")),
    		Material.create((Texture) assets.get("tex-concrete-barrier")), // missing
    		Material.create((Texture) assets.get("tex-concrete-barrier")),	// missing
    		Material.create((Texture) assets.get("tex-bricks01")),
    		Material.create((Texture) assets.get("tex-concrete-mix")),
    		Material.create((Texture) assets.get("tex-gravel1")),
    		Material.create((Texture) assets.get("tex-asphalt1")),
    		Material.create((Texture) assets.get("tex-lamppost-metal")),
    		Material.create((Texture) assets.get("tex-wires")),
    		Material.create((Texture) assets.get("tex-tree1")),
    		Material.create((Texture) assets.get("tex-graffiti-3-alpha")),
    		Material.create((Texture) assets.get("tex-graffiti-2-alpha")),
    		Material.create((Texture) assets.get("tex-graffiti-1-alpha")),
    		Material.create((Texture) assets.get("tex-snow")),
    		Material.create((Texture) assets.get("tex-dirt-decal2")),
    		Material.create((Texture) assets.get("tex-dirt-decal1")),
    		Material.create((Texture) assets.get("tex-barrel-metal")),
    		Material.create((Texture) assets.get("tex-barrel-trash"))
    	);*/
        
        	// Outside
        TestModel testScene = new TestModel(model);
        //testScene.getTransform().setScale(0.01f, 0.01f, 0.01f);
        //testScene.getTransform().setScale(1f, 1f, 1f);
		this.worldScene.addObject(testScene);
		
			// Player
		//model = new Model((Mesh) assets.get("mesh-player-skinned"));
		model = new Model((Mesh) assets.get("mesh-car-test"));
		model.setMaterial(Material.create((Texture) assets.get("tex-car-test")));
		
		TestPlayer testPlayer = new TestPlayer(model);
		testPlayer.getTransform().setPosition((float)Math.random()*10f, 15, (float)Math.random()*10f);
		//testPlayer.getTransform().setPosition(150f, 30, -150f);
		//testPlayer.getTransform().setScale(0.01f, .01f, .01f);
		testPlayer.getAnimator().setSpeed(1/30f);
		testPlayer.getAnimator().setAnimation((Animation) assets.get("anim-player-idle"));
		this.worldScene.addObject((IPhysicsObject) testPlayer);
		
		TestPlane testPlane = new TestPlane();
		this.worldScene.addObject((IPhysicsObject) testPlane); 
		
		Logger.info(this, "Game setup done!");
	}
	
	private void preloadAssets() {
		AssetManager.Group preload = new AssetManager.Group("preload");
		
				///////////////// TEXTURES
			preload.put(new Texture("tex-default", FileUtils.getResourcePath("texture/texture.png")));
			preload.put(new Texture("tex-asphalt1", FileUtils.getResourcePath("texture/outside/diffuse/asphalt1_diff.png")));
			preload.put(new Texture("tex-barrel-metal", FileUtils.getResourcePath("texture/outside/diffuse/barrel_metal_diff.png")));
			preload.put(new Texture("tex-barrel-trash", FileUtils.getResourcePath("texture/outside/diffuse/barrel_trash_diff.png")));
			preload.put(new Texture("tex-bricks01", FileUtils.getResourcePath("texture/outside/diffuse/bricks01_diff.png")));
			preload.put(new Texture("tex-concrete-barrier", FileUtils.getResourcePath("texture/outside/diffuse/concrete_barrier_diff.png")));
			preload.put(new Texture("tex-concrete-barrier-dirty", FileUtils.getResourcePath("texture/outside/diffuse/concrete_barrier_dirty_diff.png")));
			preload.put(new Texture("tex-concrete-mix", FileUtils.getResourcePath("texture/outside/diffuse/concrete_mix_diff.png")));
			preload.put(new Texture("tex-concrete-wall-1", FileUtils.getResourcePath("texture/outside/diffuse/concrete_wall1_diff.png")));
			preload.put(new Texture("tex-concrete-wall-2", FileUtils.getResourcePath("texture/outside/diffuse/concrete_wall2_diff.png")));
			preload.put(new Texture("tex-concrete-block-1", FileUtils.getResourcePath("texture/outside/diffuse/concrete_block1_diff.png")));
			preload.put(new Texture("tex-dirt-decal1", FileUtils.getResourcePath("texture/outside/diffuse/dirt_decal1_diff.png")));
			preload.put(new Texture("tex-dirt-decal2", FileUtils.getResourcePath("texture/outside/diffuse/dirt_decal2_diff.png")));
			preload.put(new Texture("tex-graffiti-1-alpha", FileUtils.getResourcePath("texture/outside/diffuse/graffiti1_alpha.png")));
			preload.put(new Texture("tex-graffiti-2-alpha", FileUtils.getResourcePath("texture/outside/diffuse/graffiti2_alpha.png")));
			preload.put(new Texture("tex-graffiti-3-alpha", FileUtils.getResourcePath("texture/outside/diffuse/graffiti3_alpha.png")));
			preload.put(new Texture("tex-gravel1", FileUtils.getResourcePath("texture/outside/diffuse/gravel1_diff.png")));
			preload.put(new Texture("tex-lampbox-metal", FileUtils.getResourcePath("texture/outside/diffuse/lampbox_metal_diff.png")));
			preload.put(new Texture("tex-lamppost-metal", FileUtils.getResourcePath("texture/outside/diffuse/lamppost_metal_diff.png")));
			preload.put(new Texture("tex-metal-dirtyrust", FileUtils.getResourcePath("texture/outside/diffuse/metal_dirtyrust_diff.png")));
			preload.put(new Texture("tex-metal-door1", FileUtils.getResourcePath("texture/outside/diffuse/metal_door1_diff.png")));
			preload.put(new Texture("tex-metal-skirting", FileUtils.getResourcePath("texture/outside/diffuse/metal_skirting_diff.png")));
			preload.put(new Texture("tex-pavement1", FileUtils.getResourcePath("texture/outside/diffuse/pavement1_diff.png")));
			preload.put(new Texture("tex-snow", FileUtils.getResourcePath("texture/outside/diffuse/snow_diff.png")));
			preload.put(new Texture("tex-tree1", FileUtils.getResourcePath("texture/outside/diffuse/tree1_diff.png")));
			preload.put(new Texture("tex-window-blinds1", FileUtils.getResourcePath("texture/outside/diffuse/window_blinds1.png")));
			preload.put(new Texture("tex-wires", FileUtils.getResourcePath("texture/outside/diffuse/wires_diff.png")));
			preload.put(new Texture("tex-wood-planks1", FileUtils.getResourcePath("texture/outside/diffuse/wood_planks1_diff.png")));
			
			preload.put(new Texture("tex-player", FileUtils.getResourcePath("texture/player_diff.png")));
			preload.put(new Texture("tex-car-test", FileUtils.getResourcePath("texture/placeholder.png")));
			preload.put(new Texture("tex-road-test", FileUtils.getResourcePath("texture/AsphaltDiff.png")));
			
			
				///////////////// MESHES
			preload.put(new Mesh("mesh-outside", FileUtils.getResourcePath("model/Outside.fbx"), true));
			//preload.put(new Mesh("mesh-road-test", FileUtils.getResourcePath("model/RoadTest.fbx"), true));
			preload.put(new Mesh("mesh-road-test", FileUtils.getResourcePath("model/RoadTestScale.fbx"), true, (
					Assimp.aiProcess_GenSmoothNormals |
					Assimp.aiProcess_Triangulate | 
					Assimp.aiProcess_FixInfacingNormals | 
					Assimp.aiProcess_CalcTangentSpace | 
					Assimp.aiProcess_LimitBoneWeights |
					Assimp.aiProcess_JoinIdenticalVertices |
					Assimp.aiProcess_PreTransformVertices
				)));
			//preload.put(new Mesh("mesh-car-test", FileUtils.getResourcePath("model/CarTest.fbx")));
			preload.put(new Mesh("mesh-car-test", FileUtils.getResourcePath("model/CarTestScale.fbx")));
			
			Mesh meshPlayerSkinned = new Mesh("mesh-player-skinned", FileUtils.getResourcePath("model/PlayerNoAnimScale.fbx"), false, (
				Assimp.aiProcess_GenSmoothNormals |
				Assimp.aiProcess_Triangulate | 
				Assimp.aiProcess_FixInfacingNormals | 
				Assimp.aiProcess_CalcTangentSpace | 
				Assimp.aiProcess_LimitBoneWeights
			));
			preload.put(meshPlayerSkinned);
			
			/*
			Mesh meshPlayerSkinned = new Mesh("mesh-player-skinned", FileUtils.getResourcePath("model/player.fbx"), false, (
				Assimp.aiProcess_GenSmoothNormals |
				Assimp.aiProcess_Triangulate | 
				Assimp.aiProcess_FixInfacingNormals | 
				Assimp.aiProcess_CalcTangentSpace | 
				Assimp.aiProcess_LimitBoneWeights
			));
			preload.put(meshPlayerSkinned);*/
			
				///////////////// ANIMATIONS
			Animation animPlayerIdle = new Animation("anim-player-idle", FileUtils.getResourcePath("anim/IdleAnim.fbx"));
			animPlayerIdle.DEBUGsetSkeleton(meshPlayerSkinned.getSkeleton());
			preload.put(animPlayerIdle);
			
			Animation animPlayerRun = new Animation("anim-player-run", FileUtils.getResourcePath("anim/RunAnim.fbx"));
			animPlayerRun.DEBUGsetSkeleton(meshPlayerSkinned.getSkeleton());
			preload.put(animPlayerRun);
			
			Animation animIdleSmoke = new Animation("anim-player-idle-smoke", FileUtils.getResourcePath("anim/IdleSmokeAnim.fbx"));
			animIdleSmoke.DEBUGsetSkeleton(meshPlayerSkinned.getSkeleton());
			preload.put(animIdleSmoke);
			
			Animation animGetUpFromBack = new Animation("anim-player-get-up-back", FileUtils.getResourcePath("anim/GetUpFromBackAnim.fbx"));
			animGetUpFromBack.DEBUGsetSkeleton(meshPlayerSkinned.getSkeleton());
			preload.put(animGetUpFromBack);
		
		this.assetManager.registerGroup(preload);
		preload.load();
	}
	
	@Override
	public void tick(float deltaTime) {
		this.worldScene.getActiveCamera().getProjection().setAspectRatio(Engine.getWindow().getWidth() / (float) Engine.getWindow().getHeight());
		this.worldScene.tick(deltaTime);
		
		Window window = Engine.getWindow();
		window.getInput().DEBUGmapInput(11306, (e) -> {
			window.enableCursor(!window.isCursorEnabled());
		});
	}
	
	public IScene getWorldScene() {
		return this.worldScene;
	}
	
	public AssetManager.Group getAssets() {
		return this.assetManager.findGroup("preload");
	}
}
