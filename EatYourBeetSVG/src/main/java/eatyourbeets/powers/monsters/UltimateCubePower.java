package eatyourbeets.powers.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class UltimateCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UltimateCubePower.class);

    private static final int BUFFS_AMOUNT = 1;
    private static final int EXPLOSION_DAMAGE = 140;

    private final RandomizedList<ActionT1<AbstractCreature>> buffs1 = new RandomizedList<>();
    private final RandomizedList<ActionT1<AbstractCreature>> buffs2 = new RandomizedList<>();
    private boolean buffSwitch = false;

    public UltimateCubePower(AbstractCreature owner, int countDown)
    {
        super(owner, POWER_ID);

        amount = countDown;

        priority = -100;

        updateDescription();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (amount >= 0)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + BUFFS_AMOUNT + desc[1] + amount + desc[2] + EXPLOSION_DAMAGE + desc[3];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        for (int i = 0; i < BUFFS_AMOUNT; i++)
        {
            GainRandomBuff(owner);
        }
    }

    @Override
    public void duringTurn()
    {
        super.duringTurn();

        if (amount > 0)
        {
            this.amount -= 1;
            updateDescription();

            if (amount <= 0)
            {
                Explode();
            }
        }
    }

    private void Explode()
    {
        int damageStep = EXPLOSION_DAMAGE / 20;
        for (int i = 0; i < 20; i++)
        {
            final float x = owner.hb.cX + MathUtils.random(-40, 40);
            final float y = owner.hb.cY + MathUtils.random(-40, 40);

            GameActions.Bottom.Wait(0.3f);
            GameActions.Bottom.VFX(new ExplosionSmallEffect(x, y), 0f);
            GameActions.Bottom.DealDamage(owner, player, damageStep, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
        }

        GameActions.Bottom.Add(new SuicideAction((AbstractMonster)this.owner));
    }

    private void GainRandomBuff(AbstractCreature c)
    {
        if (buffs1.Size() == 0)
        {
            buffs1.Add(this::BuffLightning);
            buffs1.Add(this::BuffFire);
            buffs1.Add(this::BuffDark);
        }

        if (buffs2.Size() == 0)
        {
            buffs2.Add(this::BuffHealing);
            buffs2.Add(this::BuffFrost);
        }

        if (buffSwitch = !buffSwitch)
        {
            buffs2.Retrieve(rng).Invoke(c);
        }
        else
        {
            buffs1.Retrieve(rng).Invoke(c);
        }
    }

    private void BuffHealing(AbstractCreature c)
    {
         GameActions.Bottom.StackPower(new HealingCubePower(c, 9));
    }

    private void BuffFire(AbstractCreature c)
    {
         GameActions.Bottom.StackPower(new FireCubePower(c, 3));
    }

    private void BuffFrost(AbstractCreature c)
    {
         GameActions.Bottom.StackPower(new FrostCubePower(c, 6));
    }

    private void BuffDark(AbstractCreature c)
    {
         GameActions.Bottom.StackPower(new DarkCubePower(c, 3));
    }

    private void BuffLightning(AbstractCreature c)
    {
         GameActions.Bottom.StackPower(new LightningCubePower(c, 4));
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        if (!player.hasBlight(eatyourbeets.blights.animator.UltimateCube.ID))
        {
            GameUtilities.ObtainBlight(player.hb.cX, player.hb.cY, new eatyourbeets.blights.animator.UltimateCube());
        }
    }
}