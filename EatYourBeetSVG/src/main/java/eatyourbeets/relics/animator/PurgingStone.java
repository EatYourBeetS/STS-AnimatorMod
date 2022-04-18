package eatyourbeets.relics.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class PurgingStone extends AnimatorRelic
{
    public static final String ID = CreateFullID(PurgingStone.class);
    public static final int ENERGY_COST = 1;
    public static final int TEMP_HP = 3;

    public PurgingStone()
    {
        super(ID, RelicTier.SHOP, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, ENERGY_COST, TEMP_HP);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        GameActions.Bottom.ApplyPower(new PurgingStonePower(player, this, TEMP_HP));
        flash();
    }

    public static class PurgingStonePower extends AnimatorClickablePower
    {
        public PurgingStonePower(AbstractCreature owner, EYBRelic relic, int amount)
        {
            super(owner, relic, PowerTriggerConditionType.Energy, ENERGY_COST);

            this.triggerCondition.SetUses(1, false, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.ExhaustFromHand(name, BaseMod.MAX_HAND_SIZE, true)
            .SetOptions(true, true, true)
            .SetFilter(GameUtilities::IsHindrance)
            .AddCallback(cards ->
            {
                int tempHP = 0;
                for (AbstractCard c : cards)
                {
                    if (c.type == AbstractCard.CardType.CURSE)
                    {
                        tempHP += amount;
                    }
                }

                if (tempHP > 0)
                {
                    GameActions.Bottom.GainTemporaryHP(tempHP);
                }
            });

            GameActions.Bottom.WaitRealtime(0.35f);
            RemovePower(GameActions.Last);
        }
    }
}