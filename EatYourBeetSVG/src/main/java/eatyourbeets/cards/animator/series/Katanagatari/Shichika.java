package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ShichikaKyotouryuu;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;

public class Shichika extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Shichika.class);
    static
    {
        staticCardData.get(ID).InitializePreview(new ShichikaKyotouryuu(), false);
    }

    public Shichika()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.MakeCardInHand(new ShichikaKyotouryuu());

        if (HasSynergy())
        {
            GameActions.Bottom.GainAgility(1);
            GameActions.Bottom.GainThorns(2);
        }
    }
}