package gameengine.game.light;

import gameengine.engine.renderer.uniform.object.attenuation.SSAttenuation;

public class Attenuation {
    private float constant;
    private float exponent;
    private float linear;
    private SSAttenuation attenuationStruct;

    public Attenuation(float constant, float linear, float exponent) {
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
        this.attenuationStruct = new SSAttenuation();
        this.updateStruct();
    }
    
    
    private void updateStruct() {
    	this.attenuationStruct.constant = this.constant;
    	this.attenuationStruct.exponent = this.exponent;
    	this.attenuationStruct.linear = this.linear;
    }
    
    public void setConstant(float constant) {
        this.constant = constant;
        this.updateStruct();
    }

    public void setExponent(float exponent) {
        this.exponent = exponent;
        this.updateStruct();
    }

    public void setLinear(float linear) {
        this.linear = linear;
        this.updateStruct();
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
    
    public SSAttenuation getAsStruct() {
    	return this.attenuationStruct;
    }
}
