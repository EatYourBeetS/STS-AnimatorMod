package eatyourbeets.cards.animatorClassic.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Verdia extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Verdia.class).SetSeriesFromClassPackage().SetSkill(3, CardRarity.COMMON).SetMaxCopies(3);

    public Verdia()
    {
        super(DATA);

        Initialize(0, 13, 2, 2);
        SetUpgrade(0, 0, 1, 1);
        SetScaling(0, 0, 1);

        
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainPlatedArmor(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.ApplyVulnerable(p, m, secondaryValue);
    }
}