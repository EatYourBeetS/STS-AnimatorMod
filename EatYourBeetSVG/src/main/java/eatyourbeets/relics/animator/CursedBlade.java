package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.utilities.GameActions;

public class CursedBlade extends AnimatorRelic
{
    public static final String ID = CreateFullID(CursedBlade.class);
    public static final int BUFF_AMOUNT = 3;
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
            GameActions.Bottom.MakeCardInHand(new Status_Wound());
            flash();
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        GameActions.Bottom.ApplyPower(new CursedBladePower(player, this));
        GameActions.Bottom.GainForce(BUFF_AMOUNT);
        GameActions.Bottom.GainAgility(BUFF_AMOUNT);
        flash();
    }

    public static class CursedBladePower extends AnimatorClickablePower
    {
        public CursedBladePower(AbstractCreature owner, EYBRelic relic)
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
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SFX(SFX.ATTACK_WHIRLWIND, 0.9f, 1.1f);
            GameActions.Bottom.VFX(VFX.Whirlwind(Color.RED, false));
            GameActions.Bottom.DealDamageToAll(DamageInfo.createDamageMatrix(CursedBlade.AOE_DAMAGE, true),
                    DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL);

            this.triggerCondition.requiredAmount += DAMAGE_AND_COST_INCREASE;
            this.amount += DAMAGE_AND_COST_INCREASE;
        }
    }
}