package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final String ID = Register(TukaLunaMarceau.class, EYBCardBadge.Discard);

    public TukaLunaMarceau()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 2, 3);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainBlock(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.currentBlock <= 0)
        {
            GameActions.Bottom.Draw(1);
        }

        GameActions.Bottom.GainBlock(this.block);
    }
}