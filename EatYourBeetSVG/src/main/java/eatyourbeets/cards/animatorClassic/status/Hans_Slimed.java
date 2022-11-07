package eatyourbeets.cards.animatorClassic.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Hans_Slimed extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Hans_Slimed.class).SetStatus(3, CardRarity.SPECIAL, EYBCardTarget.None);

    public Hans_Slimed()
    {
        super(DATA);

        Initialize(0, 0, 9);

        cropPortrait = false;
        SetExhaust(true);
        this.series = CardSeries.Konosuba;
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);

        if (upgraded)
        {
            GameActions.Bottom.Draw(1);
        }
    }
}