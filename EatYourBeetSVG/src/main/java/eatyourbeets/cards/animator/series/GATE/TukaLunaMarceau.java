package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final String ID = Register_Old(TukaLunaMarceau.class);

    public TukaLunaMarceau()
    {
        super(ID, 0, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

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