package gameengine.game.component.light;

import gameengine.engine.renderer.uniform.object.IHasStruct;
import gameengine.engine.renderer.uniform.object.attenuation.SSAttenuation;

public class Attenuation implements IHasStruct {
    private float constant;
    private float exponent;
    private float linear;
    private SSAttenuation attenuationStruct;

    public Attenuation(float constant, float linear, float exponent) {
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
        this.attenuationStruct = new SSAttenuation();
    }
    
    
    public void setConstant(float constant) {
        this.constant = constant;
    }

    public void setExponent(float exponent) {
        this.exponent = exponent;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getConstant() {
        return constant;
    }

    public float getExponent() {
        return exponent;
    }

    public float getLinear() {
        return linear;
    }
    
    @Override
    public SSAttenuation getAsStruct() {
    	this.attenuationStruct.constant = this.constant;
    	this.attenuationStruct.exponent = this.exponent;
    	this.attenuationStruct.linear = this.linear;
    	return this.attenuationStruct;
    }
}
