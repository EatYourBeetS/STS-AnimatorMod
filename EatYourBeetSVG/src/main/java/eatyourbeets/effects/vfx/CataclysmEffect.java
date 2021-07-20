package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class CataclysmEffect extends EYBEffect
{
    protected final static Hitbox sky_hb1 = new Hitbox(Settings.WIDTH * 0.52f, Settings.HEIGHT * 0.7f, 2, 2);
    protected final static Hitbox sky_hb2 = new Hitbox(Settings.WIDTH * 0.48f, Settings.HEIGHT * 0.7f, 2, 2);
    protected final RandomizedList<AbstractMonster> enemies = new RandomizedList<>();
    protected float delay_1;
    protected float delay_2;
    protected float delay_3;
    protected boolean vertical;

    public CataclysmEffect()
    {
        super(4f, true);
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        GameEffects.Queue.Add(new BorderLongFlashEffect(Color.VIOLET, false));
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        if (enemies.Size() == 0)
        {
            this.enemies.AddAll(GameUtilities.GetEnemies(true));
        }

        delay_1 -= deltaTime;
        if (delay_1 < 0)
        {
            delay_1 = Random(0.1f, 0.3f);

            AbstractMonster target = enemies.Retrieve(RNG);
            if (target != null)
            {
                GameEffects.Queue.Add(VFX.Fireball(sky_hb1, target.hb).SetColor(Color.RED, Color.ORANGE));
                GameEffects.Queue.Add(VFX.Fireball(sky_hb2, target.hb).SetColor(Color.GOLDENROD, Color.VIOLET));
                GameEffects.Queue.Add(VFX.MeteorFall(target.hb));
            }
        }

        delay_2 -= deltaTime;
        if (delay_2 < 0)
        {
            delay_2 = Random(0.6f, 1f);
            GameEffects.Queue.Add(new BorderFlashEffect(RandomBoolean(0.5f) ? Color.RED : Color.ORANGE));
            GameEffects.Queue.Add(VFX.Whirlwind());
        }

        delay_3 -= deltaTime;
        if (delay_3 < 0)
        {
            delay_3 = Random(0.4f, 0.75f);
            if (RandomBoolean(0.5f))
            {
                for (int f = 0; f < 18; f++)
                {
                    GameEffects.Queue.Add(VFX.FallingIce(18));
                }
            }
            else
            {
                for (AbstractMonster m : GameUtilities.GetEnemies(true))
                {
                    GameEffects.Queue.Add(VFX.Lightning(m.hb));
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {

    }

    @Override
    public void dispose()
    {

    }
}
