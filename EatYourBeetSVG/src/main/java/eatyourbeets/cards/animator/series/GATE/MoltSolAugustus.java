package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.MoltSolAugustus_ImperialArchers;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MoltSolAugustus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MoltSolAugustus.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    private static final int ENERGY_COST = 1;
    static
    {
        DATA.AddPreview(new MoltSolAugustus_ImperialArchers(), false);
    }

    public MoltSolAugustus()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Red(1);

        SetDelayed(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new MoltSolAugustusPower(p, secondaryValue));
    }

    public static class MoltSolAugustusPower extends AnimatorClickablePower
    {
        public static final int DRAW_REDUCTION = 1;

        public MoltSolAugustusPower(AbstractCreature owner, int amount)
        {
            super(owner, MoltSolAugustus.DATA, PowerTriggerConditionType.Energy, MoltSolAugustus.ENERGY_COST);

            triggerCondition.SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, DRAW_REDUCTION);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            GameUtilities.ModifyCardDrawPerTurn(-DRAW_REDUCTION, 1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            GameUtilities.ModifyCardDrawPerTurn(DRAW_REDUCTION, 1);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.SelectFromPile(name, Integer.MAX_VALUE, player.exhaustPile, player.drawPile, player.discardPile, player.hand)
            .SetOptions(false, false)
            .SetFilter(GameUtilities::HasRedAffinity)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    ((EYBCard)c).SetHaste(true);
                }
            });

            flashWithoutSound();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.MakeCardInDrawPile(new MoltSolAugustus_ImperialArchers())
            .Repeat(amount).SetDuration(0.1f, false);
        }
    }
}