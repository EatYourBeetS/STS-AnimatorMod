package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Hans_Slimed extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Hans_Slimed.class).SetStatus(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public Hans_Slimed()
    {
        super(DATA);

        Initialize(0, 0, 1);

        cropPortrait = false;
        SetExhaust(true);
        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);

        if (upgraded)
        {
            GameActions.Bottom.Draw(1);
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }
}