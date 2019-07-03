package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final String ID = CreateFullID(TukaLunaMarceau.class.getSimpleName());

    public TukaLunaMarceau()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 2, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.ApplyPower(p, p, new NextTurnBlockPower(p, block), block);

        if (HasActiveSynergy())
        {
            if (PlayerStatistics.getSynergiesThisTurn() == 0)
            {
                GameActionsHelper.DrawCard(p, 1);
                //GameActionsHelper.RandomCostReduction(magicNumber, 1, false);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
        }
    }
}