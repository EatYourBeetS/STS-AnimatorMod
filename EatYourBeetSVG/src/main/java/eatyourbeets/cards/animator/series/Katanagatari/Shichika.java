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

        Initialize(0, 2);
        SetUpgrade(0, 0);
        SetScaling(0, 1, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainForce(1, upgraded);
        GameActions.Bottom.GainAgility(1, upgraded);

        GameActions.Bottom.MakeCardInHand(new ShichikaKyotouryuu());
    }
}