package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.MoltSolAugustus_ImperialArchers;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MoltSolAugustus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MoltSolAugustus.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new MoltSolAugustus_ImperialArchers(), false));
    private static final int ENERGY_COST = 1;
    private static final int ARCHERS_AMOUNT = 2;

    public MoltSolAugustus()
    {
        super(DATA);

        Initialize(0, 0, 2, ARCHERS_AMOUNT);
        SetUpgrade(0, 0, 2);

        SetAffinity_Red(1);

        SetDelayed(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new MoltSolAugustusPower(p, magicNumber));
    }

    public static class MoltSolAugustusPower extends AnimatorClickablePower
    {
        public MoltSolAugustusPower(AbstractCreature owner, int amount)
        {
            super(owner, MoltSolAugustus.DATA, PowerTriggerConditionType.Energy, MoltSolAugustus.ENERGY_COST);

            triggerCondition.SetUses(1, true, true);
            canBeZero = true;

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, ARCHERS_AMOUNT, amount);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (amount > 0 && GameUtilities.HasRedAffinity(card))
            {
                GameActions.Top.Draw(1);
                reducePower(1);
                flash();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (ResetAmount() <= 0)
            {
                RemovePower();
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SFX(SFX.RELIC_DROP_FLAT);
            GameActions.Bottom.MakeCardInDrawPile(new MoltSolAugustus_ImperialArchers())
            .Repeat(ARCHERS_AMOUNT).SetDuration(0.1f, false);
            GameActions.Bottom.SFX(SFX.ANIMATOR_ARROW);
        }
    }
}