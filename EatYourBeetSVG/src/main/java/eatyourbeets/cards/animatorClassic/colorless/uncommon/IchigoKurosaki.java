package eatyourbeets.cards.animatorClassic.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.IchigoKurosaki_Bankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IchigoKurosaki extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(IchigoKurosaki.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Bleach);
    static
    {
        DATA.AddPreview(new IchigoKurosaki_Bankai(), false);
    }

    public IchigoKurosaki()
    {
        super(DATA);

        Initialize(0, 0, 0, 5);

        SetExhaust(true);
        SetMartialArtist();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(() ->
        {
            if (GameUtilities.GetPowerAmount(Affinity.Red) > secondaryValue)
            {
                GameActions.Bottom.MakeCardInDrawPile(new IchigoKurosaki_Bankai());
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainForce(1, true);
        GameActions.Bottom.GainAgility(1, true);

        if (upgraded)
        {
            GameActions.Bottom.Draw(1);
        }
    }
}