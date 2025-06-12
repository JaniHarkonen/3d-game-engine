package gameengine.engine.physics;

public interface IPhysicsObject extends IHasTransform {

	public void onPhysicsCreate(PhysicsScene scene);
	public Physics getPhysics();
}
