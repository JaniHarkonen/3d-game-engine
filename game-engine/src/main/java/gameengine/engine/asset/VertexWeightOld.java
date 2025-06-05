package gameengine.engine.asset;

class VertexWeightOld {

	private int boneID;
	private int vertexID;
	private float weight;
	
	public VertexWeightOld(int boneID, int vertexID, float weight) {
		this.boneID = boneID;
		this.vertexID = vertexID;
		this.weight = weight;
	}
	
	
	public int getBoneID() {
		return this.boneID;
	}
	
	public int getVertexID() {
		return this.vertexID;
	}
	
	public float getWeight() {
		return this.weight;
	}
}
