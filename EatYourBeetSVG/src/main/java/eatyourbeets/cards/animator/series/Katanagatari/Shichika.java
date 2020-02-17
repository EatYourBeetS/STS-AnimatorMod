package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ShichikaKyotouryuu;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;

public class Shichika extends AnimatorCard implements MartialArtist
{
    public static final EYBCardData DATA = Register(Shichika.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new ShichikaKyotouryuu(), false);
    }

    public Shichika()
    {
        super(DATA);

        Initialize(0, 3, 1);
        SetUpgrade(0, 0, 1);
        SetScaling(0, 1, 0);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainForce(magicNumber);

        if (HasSynergy())
        {
            GameActions.Bottom.GainAgility(1);
        }

        GameActions.Bottom.MakeCardInHand(new ShichikaKyotouryuu());
    }
}