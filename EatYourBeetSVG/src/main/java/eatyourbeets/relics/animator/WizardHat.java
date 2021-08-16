package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class WizardHat extends AnimatorRelic
{
    public static final String ID = CreateFullID(WizardHat.class);
    public static final int INTELLECT_AMOUNT = 2;
    public static final int DAMAGE_AMOUNT = 32;
    public static final int ENERGY_COST = 4;

    public WizardHat()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, INTELLECT_AMOUNT) + " NL " + FormatDescription(1, ENERGY_COST, DAMAGE_AMOUNT);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        GameActions.Bottom.ApplyPower(new WizardHarPower(player, this));
        GameActions.Bottom.GainIntellect(INTELLECT_AMOUNT);
        flash();
    }

    public static class WizardHarPower extends AnimatorClickablePower
    {
        public WizardHarPower(AbstractCreature owner, EYBRelic relic)
        {
            super(owner, relic, PowerTriggerConditionType.Energy, ENERGY_COST);

            this.amount = DAMAGE_AMOUNT;
            this.triggerCondition.SetUses(1, false, false);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(1, triggerCondition.requiredAmount, amount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.9f, 1.1f);
            GameActions.Bottom.Wait(0.35f);
            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.8f, 1.2f);
            GameActions.Bottom.BorderFlash(Color.ORANGE);
            GameActions.Bottom.Wait(0.35f);
            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.7f, 1.3f);
            GameActions.Bottom.Wait(0.35f);
            GameActions.Bottom.BorderFlash(Color.RED);
            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.5f, 1.5f);

            for (AbstractCreature c : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.VFX((new FlameBarrierEffect(c.hb_x, c.hb_y)));
                GameActions.Bottom.VFX((new ExplosionSmallEffect(c.hb_x, c.hb_y)));
            }

            int[] multiDamage = DamageInfo.createDamageMatrix(DAMAGE_AMOUNT, true);
            GameActions.Bottom.DealDamageToAll(multiDamage, DamageInfo.DamageType.THORNS, AttackEffects.NONE);
        }
    }
}