package com.gdxjam.magellan.ships;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.magellan.*;
import com.gdxjam.magellan.gameobj.*;

/**
 * Created by lolcorner on 19.12.2015.
 */
public class Ship extends MovingGameObj implements IDrawableMap, IDrawableWindow, IDestroyable, IArmed {

    public float shield = .1f;
    public int health = 3;
    public int attack = 1;
    public Sprite spriteDot;
    public Array<ParticleEffect> effects;
    public ParticleEffect trail;

    public Ship(Sector sector) {
        super(sector);
    }

    @Override
    public int shootAt(IDestroyable target) {
        target.receiveDamage(attack);
        return attack;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public void receiveDamage(int damage) {
        if(Math.random() < shield) return;
        health -= damage;
        if(health <= 0) destroy();
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void destroy() {
        dispose();
    }

    @Override
    public void dispose() {
        this.sector.gameObjs.removeValue(this, true);
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public float getShield() {
        return shield;
    }

    @Override
    public String getTitle() {
        return "SHIP";
    }

    @Override
    public String getInfo() {
        String s = "Faction: " + faction.toString();
        s += "\nHealth: " + health;
        s += "\nAttack: " + attack;
        s += "\nShield: " + Math.round(shield * 100) + "%";
        return s;
    }

    @Override
    public Actor getActor() {
        Image image = new Image(MagellanGame.assets.get("map_playership.png", Texture.class));
        image.setUserObject(this);
        return image;
    }

    @Override
    public void prepareRenderingOnMap() {
        trail = new ParticleEffect();
        trail.load(Gdx.files.internal("effects.p"),Gdx.files.internal(""));
        effects.add(trail);
    }

    @Override
    public void renderOnMap(SpriteBatch batch, float delta) {
        for(ParticleEffect pe : effects){
            pe.draw(batch, delta);
            if(pe.isComplete()) effects.removeValue(pe, true);
        }
    }

    @Override
    public void moveTo(Sector sector) {
        super.moveTo(sector);
        trail.start();
    }
}
