# TODO

- make IGame or AGame to completely separate 'engine' package from the 'game' package
- implement dispose methods for appropriate classes
- implement instance IDs to avoid hash collisions with the default Object.hashCode
- refactor mesh loading so that primitive arrays, such as float[] and int[] arrays are loaded in first and then converted into arrays of Vector3fs and Faces
	-> this will make the code less messy and allows the primitive arrays to be instantly passed onto collision mesh generation
- consider if using primitive arrays instead of faces and vectors would be a better idea for mesh data
	-> primitive arrays are used by VAOs and collision triangle mesh generation
	-> use up less memory
	-> could be converted into objects when need be
- implement debug coloring for collisions
