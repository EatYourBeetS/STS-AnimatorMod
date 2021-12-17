package pinacolada.relics.pcl;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class WizardHat extends PCLRelic
{
    public static final String ID = CreateFullID(WizardHat.class);
    public static final int INTELLECT_AMOUNT = 2;
    public static final int DAMAGE_BONUS_PER_TURN = 2;
    public static final int DAMAGE_AMOUNT = 14;
    public static final int ENERGY_COST = 3;

    public WizardHat()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, INTELLECT_AMOUNT) + " NL " + FormatDescription(1, ENERGY_COST, DAMAGE_AMOUNT, DAMAGE_BONUS_PER_TURN);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        PCLActions.Bottom.ApplyPower(new WizardHarPower(player, this));
        PCLActions.Bottom.GainWisdom(INTELLECT_AMOUNT);
        flash();
    }

    public static class WizardHarPower extends PCLClickablePower
    {
        public WizardHarPower(AbstractCreature owner, PCLRelic relic)
        {
            super(owner, relic, PowerTriggerConditionType.Energy, ENERGY_COST);

            this.amount = DAMAGE_AMOUNT;
            this.triggerCondition.SetUses(1, false, false);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(1, triggerCondition.requiredAmount, amount, DAMAGE_BONUS_PER_TURN);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.9f, 1.1f);
            PCLActions.Bottom.Wait(0.35f);
            PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.8f, 1.2f);
            PCLActions.Bottom.BorderFlash(Color.ORANGE);
            PCLActions.Bottom.Wait(0.35f);
            PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.7f, 1.3f);
            PCLActions.Bottom.Wait(0.35f);
            PCLActions.Bottom.BorderFlash(Color.RED);
            PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.5f, 1.5f);

            for (AbstractCreature c : PCLGameUtilities.GetEnemies(true))
            {
                PCLActions.Bottom.VFX((new FlameBarrierEffect(c.hb_x, c.hb_y)));
                PCLActions.Bottom.VFX((new ExplosionSmallEffect(c.hb_x, c.hb_y)));
            }

            int[] multiDamage = DamageInfo.createDamageMatrix(amount, true);
            PCLActions.Bottom.DealDamageToAll(multiDamage, DamageInfo.DamageType.THORNS, AttackEffects.NONE);
            PCLActions.Bottom.WaitRealtime(0.35f);
            RemovePower(PCLActions.Last);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            stackPower(DAMAGE_BONUS_PER_TURN);
        }
    }
}