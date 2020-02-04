package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.IchigoBankai;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IchigoKurosaki extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register_Old(IchigoKurosaki.class);
    static
    {
        GetStaticData(ID).InitializePreview(new IchigoBankai(), false);
    }

    public IchigoKurosaki()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1, 5);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(__ ->
        {
            if (GameUtilities.GetStrength() >= secondaryValue)
            {
                GameActions.Bottom.MakeCardInDrawPile(new IchigoBankai());
            }
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainAgility(1);
    }
}