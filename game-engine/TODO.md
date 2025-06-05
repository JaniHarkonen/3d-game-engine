# TODO

- make IGame or AGame to completely separate 'engine' package from the 'game' package
- implement default assets
- implement dispose methods for appropriate classes
- implement Bone, Node and VertexWeight as static classes belonging to Mesh and Animation assets
- see if materials could bind textures instead of having an entity bind them one by one
	-> fix for model and shadow buffer
- there shouldn't be a cascade shadow list, instead use an array since this the cascade count should be fixed
- use directional light as the source for cascade shadows
