package gameengine.util;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import gameengine.engine.renderer.component.Submesh;

public final class GeometryShapeUtils {
	private static final float PI = (float) Math.PI;

	public static class Drawer {
		
			// WARNING!! THIS CLASS DOESN'T HAVE OFFSETS YET, AS IT'S ONLY BEING USED TO DRAW DEFAULT
			// SHAPES AT THE MOMENT
		private List<Vector3f> vertices;
		private List<Vector2f> UVs;
		private List<Submesh.Face> faces;
		
		public Drawer() {
			this.vertices = new ArrayList<>();
			this.UVs = null;	// Set to null on purpose
			this.faces = new ArrayList<>();
		}
		
		
		public void populateSubmesh(Submesh target) {
	        Vector3f[] normals = new Vector3f[vertices.size()];
	        for( int i = 0; i < normals.length; i++ ) {
	        	normals[i] = new Vector3f(0, 0, 1);
	        }
	        
	        Vector2f[] UVs;
	        
	        if( this.UVs == null ) {
	        	UVs = new Vector2f[this.vertices.size()];
	        	
		        for( int i = 0; i < UVs.length; i++ ) {
		        	UVs[i] = new Vector2f((float) Math.random(), (float) Math.random());
		        }
	        } else {
	        	UVs = this.UVs.toArray(new Vector2f[this.vertices.size()]);
	        }
	        
			target.populate(
				this.vertices.toArray(new Vector3f[this.vertices.size()]), 
				normals, 
				UVs, 
				this.faces.toArray(new Submesh.Face[this.faces.size()]), 
				new int[1], 
				new float[1]
			);
		}
		
		public void reset() {
				// Reset by creating new instances as ArrayList.clear only resets the pointer
				// while the capacity still remains, which could end up consuming a lot of 
				// memory if many high-poly shapes are created
			this.vertices = new ArrayList<>();
			this.UVs = null;
			this.faces = new ArrayList<>();
		}
		
		
		private void addVertex(float x, float y, float z) {
			this.vertices.add(new Vector3f(x, y, z));
		}
		
		private void addFace(int index1, int index2, int index3) {
			this.faces.add(new Submesh.Face(index1, index2, index3));
		}
		
		private void addUV(float u, float v) {
			if( this.UVs == null ) {
				this.UVs = new ArrayList<>();
			}

			this.UVs.add(new Vector2f(u, v));
		}
		
			// WARNING: HAS NO ROTATION YET EITHER
		public void drawPlane(float r) {
			float rh = r / 2;
			int offset = this.vertices.size();
			
			this.addVertex(-rh, -rh, 0);
			this.addVertex(rh, -rh, 0);
			this.addVertex(rh, rh, 0);
			this.addVertex(rh, rh, 0);
			
			this.addFace(offset, offset + 1, offset + 3);
			this.addFace(offset + 3, offset + 2, offset);
		}
		
		public void drawBox(float halfX, float halfH, float halfZ) {
			int index = this.vertices.size();
			
			// VERTICES
			
				// Front
			this.addVertex(-halfX, halfH, halfZ);
			this.addVertex(-halfX, -halfH, halfZ);
			this.addVertex(halfX, -halfH, halfZ);
			this.addVertex(halfX, halfH, halfZ);

				// Back
			this.addVertex(-halfX, halfH, -halfZ);
			this.addVertex(halfX, halfH, -halfZ);
			this.addVertex(-halfX, -halfH, -halfZ);
			this.addVertex(halfX, -halfH, -halfZ);

				// Top
			this.addVertex(-halfX, halfH, -halfZ);
			this.addVertex(halfX, halfH, -halfZ);
			this.addVertex(-halfX, halfH, halfZ);
			this.addVertex(halfX, halfH, halfZ);

				// Right
			this.addVertex(halfX, halfH, halfZ);
			this.addVertex(halfX, -halfH, halfZ);

				// Left
			this.addVertex(-halfX, halfH, halfZ);
			this.addVertex(-halfX, -halfH, halfZ);

				// Bottom
			this.addVertex(-halfX, -halfH, -halfZ);
			this.addVertex(halfX, -halfH, -halfZ);
			this.addVertex(-halfX, -halfH, halfZ);
			this.addVertex(halfX, -halfH, halfZ);
			
			
			// UVS
			
				// Front
			this.addUV(0, 1);
			this.addUV(0, 0);
			this.addUV(1, 0);
			this.addUV(1, 1);
	
				// Back
			this.addUV(0, 1);
			this.addUV(1, 1);
			this.addUV(0, 0);
			this.addUV(1, 0);
	
				// Top
			this.addUV(0, 1);
			this.addUV(1, 1);
			this.addUV(0, 1);
			this.addUV(1, 1);

				// Right
			this.addUV(1, 1);
			this.addUV(1, 0);

				// Left
			this.addUV(0, 1);
			this.addUV(0, 0);

				// Bottom
			this.addUV(0, 0);
			this.addUV(1, 0);
			this.addUV(0, 0);
			this.addUV(1, 0);
			
			// FACES
			
				// Front
			this.addFace(index, index + 1, index + 3);
			this.addFace(index + 3, index + 1, index + 2);

				// Top
			this.addFace(index + 8, index + 10, index + 11);
			this.addFace(index + 9, index + 8, index + 11);

				// Right
			this.addFace(index + 12, index + 13, index + 7);
			this.addFace(index + 5, index + 12, index + 7);

				// Left
			this.addFace(index + 6, index + 15, index + 14);
			this.addFace(index + 6, index + 14, index + 4);

				// Bottom
			this.addFace(index + 19, index + 18, index + 16);
			this.addFace(index + 19, index + 16, index + 17);

				// Back
			this.addFace(index + 7, index + 6, index + 4);
			this.addFace(index + 7, index + 4, index + 5);
		}
		
		public void drawHalfSphereTop(int resolution, float r, float yOffset, boolean vFlip) {
			int offset = this.vertices.size();
			
			if( vFlip ) {
				r *= -1;
			}
			
	        for( int i = 0; i <= resolution; i++ ) {
	            float angleY = PI * i / resolution; // latitude angle
	            
	            for( int j = 0; j <= resolution; j++ ) {
	                float angleX = 2 * PI * j / resolution; // longitude angle
	                
	                	// Parametric equations for the sphere
	                float x = r * (float) (Math.sin(angleY) * Math.cos(angleX));
	                float z = r * (float) (Math.sin(angleY) * Math.sin(angleX));
	                float y = yOffset + (r * (float) (Math.cos(angleY)));
	                
	                int index1 = offset + (i * resolution + j);
	                int index2 = offset + (i * resolution + j - 1);
	                int index3 = offset + ((i - 1) * resolution + j - 1);
	                int index4 = offset + ((i - 1) * resolution + j);
	                
                	if( vFlip ) {
	                	this.faces.add(new Submesh.Face(index3, index2, index1));
	                	this.faces.add(new Submesh.Face(index1, index4, index3));
	                } else {
	                	this.faces.add(new Submesh.Face(index1, index2, index3));
	                	this.faces.add(new Submesh.Face(index3, index4, index1));
	                }
	                
	                this.vertices.add(new Vector3f(x, y, z));
	            }
	        }
	        
	        	// This is a bug, fix later
	        for( int i = 0; i < resolution * resolution; i++ ) {
	        	this.vertices.add(new Vector3f(0, 0, 0));
	        }
		}
		
		public void drawHalfPipe(int resolution, float r, float h, boolean hFlip) {
			int index = this.vertices.size();
			
			for( int i = 0; i <= resolution; i++ ) {
				float angle = PI * i / resolution;
				float x = (float) Math.cos(angle) * r;
				float z = (float) Math.sin(angle) * r;
				float y = h / 2;
				
				if( hFlip ) {
					this.faces.add(new Submesh.Face(index + 2, index + 1, index));
					this.faces.add(new Submesh.Face(index + 2, index + 3, index + 1));
					x *= -1;
					z *= -1;
				} else {
					this.faces.add(new Submesh.Face(index + 2, index + 1, index));
					this.faces.add(new Submesh.Face(index + 2, index + 3, index + 1));
				}
				
				this.vertices.add(new Vector3f(x, y, z));
				this.vertices.add(new Vector3f(x, -y, z));
				index += 2;
			}
		}
		
		public void drawCircularHalfPlane(int resolution, float r, float y, boolean hFlip, boolean vFlip) {
			int index = this.vertices.size();
			int originIndex = index;
			
			this.vertices.add(new Vector3f(0, y, 0));
			index++;
			
			for( int i = 0; i <= resolution; i++ ) {
				float angle = GeometryUtils.PI * i / resolution;
				float x = (float) Math.cos(angle) * r;
				float z = (float) Math.sin(angle) * r;
				
				if( vFlip ) {
					this.faces.add(new Submesh.Face(originIndex, index, index + 1));
				} else {
					this.faces.add(new Submesh.Face(index + 1, index, originIndex));
				}
				
				if( hFlip ) {
					this.vertices.add(new Vector3f(-x, y, -z));
				} else {
					this.vertices.add(new Vector3f(x, y, z));
				}
				
				index++;
			}
		}
	}
	
	
	public static void populatePlane(Submesh target, float r) {
		Drawer drawer = new Drawer();
		drawer.drawPlane(r);
		drawer.populateSubmesh(target);
	}
	
	public static void populateBox(Submesh target, float halfX, float halfH, float halfZ) {
		Drawer drawer = new Drawer();
		drawer.drawBox(halfX, halfH, halfZ);
		drawer.populateSubmesh(target);
	}
	
	public static void populateSphere(Submesh target, int resolution, float r) {
		Drawer drawer = new Drawer();
		drawer.drawHalfSphereTop(resolution, r / 2, 0, false);
		drawer.drawHalfSphereTop(resolution, r / 2, 0, true);
		drawer.populateSubmesh(target);
	}
	
	public static void populateCylinder(Submesh target, int resolution, float r, float h) {
		Drawer drawer = new Drawer();
		drawer.drawCircularHalfPlane(resolution, r, h / 2, false, false);
		drawer.drawCircularHalfPlane(resolution, r, h / 2, true, false);
		
		drawer.drawCircularHalfPlane(resolution, r, -h / 2, false, true);
		drawer.drawCircularHalfPlane(resolution, r, -h / 2, true, true);
		
		drawer.drawHalfPipe(resolution, r, h, false);
		drawer.drawHalfPipe(resolution, r, h, true);
		
		drawer.populateSubmesh(target);
	}
	
	public static void populateCapsule(Submesh target, int resolution, float r, float h) {
		Drawer drawer = new Drawer();
		drawer.drawHalfPipe(resolution, r, h, false);
		drawer.drawHalfPipe(resolution, r, h, true);
		drawer.drawHalfSphereTop(resolution, r, h / 2, false);
		drawer.drawHalfSphereTop(resolution, r, -h / 2, true);
		
		drawer.populateSubmesh(target);
	}
}
