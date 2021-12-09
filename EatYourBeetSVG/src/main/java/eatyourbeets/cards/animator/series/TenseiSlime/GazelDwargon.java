package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GazelDwargon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GazelDwargon.class)
            .SetPower(-1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public GazelDwargon()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Orange(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);
        if (stacks > 0)
        {
            GameActions.Bottom.GainPlatedArmor(stacks + secondaryValue);
        }
        GameActions.Bottom.StackPower(new GazelDwargonPower(p, stacks * magicNumber));
    }

    public class GazelDwargonPower extends AnimatorPower
    {
        public GazelDwargonPower(AbstractPlayer owner, int amount)
        {
            super(owner, GazelDwargon.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.Affinities.GetPower(Affinity.Orange).SetEnabled(true);
        }


        @Override
        public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
            super.onChangeStance(oldStance,newStance);

            GameActions.Last.Callback(() -> {
                CombatStats.Affinities.GetPower(Affinity.Orange).SetEnabled(true);
            });
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (isPlayer)
            {
                CombatStats.BlockRetained += amount;
            }
        }
    }
}