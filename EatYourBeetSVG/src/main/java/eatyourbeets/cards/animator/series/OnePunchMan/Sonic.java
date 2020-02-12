package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;

public class Sonic extends AnimatorCard implements MartialArtist
{
    public static final EYBCardData DATA = Register(Sonic.class).SetPower(1, CardRarity.UNCOMMON);
    static
    {
        DATA.AddPreview(ThrowingKnife.GetCardForPreview(), false);
    }

    public Sonic()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlur(magicNumber);
        GameActions.Bottom.GainAgility(secondaryValue);

        if (HasSynergy())
        {
            GameActions.Bottom.CreateThrowingKnives(1);
        }
    }
}