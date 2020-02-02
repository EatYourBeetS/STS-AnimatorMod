package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;

public class Sonic extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Sonic.class);
    static
    {
        GetStaticData(ID).InitializePreview(ThrowingKnife.GetCardForPreview(), false);
    }

    private static final int BLOCK_ON_SYNERGY = 3;

    public Sonic()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainAgility(secondaryValue);
        GameActions.Bottom.CreateThrowingKnives(magicNumber);

        if (HasSynergy())
        {
            GameActions.Bottom.GainBlock(BLOCK_ON_SYNERGY);
            GameActions.Bottom.GainBlur(1);
        }
    }
}