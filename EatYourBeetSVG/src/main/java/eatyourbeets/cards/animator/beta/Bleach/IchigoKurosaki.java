package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.IchigoBankai;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;

public class IchigoKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IchigoKurosaki.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new IchigoBankai(), false);
    }

    public IchigoKurosaki()
    {
        super(DATA);

        Initialize(0, 0, 0, ForcePower.GetThreshold(4));

        SetExhaust(true);
        SetSynergy(Synergies.Bleach);
        SetMartialArtist();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(() ->
        {
            if (ForcePower.GetCurrentLevel() > 4)
            {
                GameActions.Bottom.MakeCardInDrawPile(new IchigoBankai());
            }
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainForce(1, true);
        GameActions.Bottom.GainAgility(1, true);

        if (upgraded)
        {
            GameActions.Bottom.Draw(1);
        }
    }
}