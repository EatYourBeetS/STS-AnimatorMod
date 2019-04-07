package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.relics.ExquisiteBloodVial;

public class ExquisiteBloodVialIncreaseCounterEffect extends AbstractGameEffect
{
    private final ExquisiteBloodVial exquisiteBloodVial;
    private final BloodVial bloodVial;

    public ExquisiteBloodVialIncreaseCounterEffect(ExquisiteBloodVial exquisiteBloodVial, BloodVial bloodVial)
    {
        this.duration = 1.0F;
        this.exquisiteBloodVial = exquisiteBloodVial;
        this.bloodVial = bloodVial;
    }

    public void update()
    {
        AbstractDungeon.player.relics.remove(bloodVial);
        AbstractDungeon.player.reorganizeRelics();
        exquisiteBloodVial.IncreaseCounter();
        this.isDone = true;
    }

    public void render(SpriteBatch sb)
    {

    }

    public void dispose()
    {

    }
}