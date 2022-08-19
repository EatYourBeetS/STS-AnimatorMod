package eatyourbeets.cards.animatorClassic.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class TukaLunaMarceau extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(TukaLunaMarceau.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public TukaLunaMarceau()
    {
        super(DATA);

        Initialize(0, 2);
        SetUpgrade(0, 2);

        SetSeries(CardSeries.GATE);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        Refresh(null);

        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.currentBlock <= 0)
        {
            GameActions.Bottom.Draw(1);
        }

        GameActions.Bottom.GainBlock(block);
    }
}