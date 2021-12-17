package pinacolada.relics.pcl;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.pcl.status.Status_Wound;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;

public class CursedBlade extends PCLRelic
{
    public static final String ID = CreateFullID(CursedBlade.class);
    public static final int BUFF_AMOUNT = 5;
    public static final int HP_COST = 3;
    public static final int DAMAGE_AND_COST_INCREASE = 2;
    public static final int AOE_DAMAGE = 9;

    public CursedBlade()
    {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, BUFF_AMOUNT) + " NL " + FormatDescription(1, HP_COST, AOE_DAMAGE, DAMAGE_AND_COST_INCREASE);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > player.currentBlock)
        {
            PCLActions.Bottom.MakeCardInHand(new Status_Wound());
            flash();
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        PCLActions.Bottom.ApplyPower(new CursedBladePower(player, this));
        PCLActions.Bottom.GainMight(BUFF_AMOUNT);
        PCLActions.Bottom.GainVelocity(BUFF_AMOUNT);
        flash();
    }

    public static class CursedBladePower extends PCLClickablePower
    {
        public CursedBladePower(AbstractCreature owner, PCLRelic relic)
        {
            super(owner, relic, PowerTriggerConditionType.LoseHP, HP_COST);

            this.amount = CursedBlade.AOE_DAMAGE;
            this.triggerCondition.SetUses(1, true, false);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(1, triggerCondition.requiredAmount, amount, DAMAGE_AND_COST_INCREASE);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            PCLActions.Bottom.SFX(SFX.ATTACK_WHIRLWIND, 0.9f, 1.1f);
            PCLActions.Bottom.VFX(VFX.Whirlwind(Color.RED, false));
            PCLActions.Bottom.DealDamageToAll(DamageInfo.createDamageMatrix(CursedBlade.AOE_DAMAGE, true),
                    DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL);

            this.triggerCondition.requiredAmount += DAMAGE_AND_COST_INCREASE;
            this.amount += DAMAGE_AND_COST_INCREASE;
        }
    }
}