# TODO

- improve rendering
- improve uniforms
- IAssets may have to implement a getPath-method
- make IGame or AGame to completely separate 'engine' package from the 'game' package
- ambient light could be a vector4f
- re-implement render strategies for AGameObjects such, that:
	- the render()-method of renderables is called by an ambiguous IRenderPass
	- the renderable has an array where its render strategies are indexed by a "render pass" ID
	- correct render strategy is dispatched, if one has been assigned
	- this will make IHasShadow useless
	- this allows for the setting of any number of rendering strategies
