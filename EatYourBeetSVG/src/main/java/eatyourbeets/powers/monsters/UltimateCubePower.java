package eatyourbeets.powers.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.blights.animator.UltimateCubeBlight;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class UltimateCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UltimateCubePower.class);
    public static final int BUFFS_AMOUNT = 1;
    public static final int EXPLOSION_DAMAGE = 140;

    private final RandomizedList<ActionT1<AbstractCreature>> buffs1 = new RandomizedList<>();
    private final RandomizedList<ActionT1<AbstractCreature>> buffs2 = new RandomizedList<>();
    private boolean buffSwitch = false;

    public UltimateCubePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        priority = -100;

        Initialize(amount);
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
        this.description = FormatDescription(0, BUFFS_AMOUNT, amount, EXPLOSION_DAMAGE);
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
            GameActions.Bottom.Wait(0.3f);
            GameActions.Bottom.VFX(VFX.SmallExplosion(owner.hb, 0.3f), 0f);
            GameActions.Bottom.DealDamage(owner, player, damageStep, DamageInfo.DamageType.THORNS, AttackEffects.NONE);
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

        if (!player.hasBlight(UltimateCubeBlight.ID))
        {
            GameUtilities.ObtainBlight(player.hb.cX, player.hb.cY, new UltimateCubeBlight());
        }
    }
}