package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.MoltSolAugustus_ImperialArchers;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MoltSolAugustus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MoltSolAugustus.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new MoltSolAugustus_ImperialArchers(), false));

    public MoltSolAugustus()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        SetAffinity_Light(2);
        SetAffinity_Earth(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new MoltSolAugustusPower(p, secondaryValue));
    }

    public static class MoltSolAugustusPower extends AnimatorPower
    {
        public static final int DRAW_REDUCTION = 1;

        public MoltSolAugustusPower(AbstractCreature owner, int amount)
        {
            super(owner, MoltSolAugustus.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, DRAW_REDUCTION);
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
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Bottom.MakeCardInHand(new MoltSolAugustus_ImperialArchers())
            .Repeat(amount).SetDuration(0.1f, false);
        }
    }
}