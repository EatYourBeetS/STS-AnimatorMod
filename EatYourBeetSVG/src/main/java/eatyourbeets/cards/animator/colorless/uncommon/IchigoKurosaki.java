package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.IchigoBankai;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IchigoKurosaki extends AnimatorCard implements MartialArtist
{
    public static final EYBCardData DATA = Register(IchigoKurosaki.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new IchigoBankai(), false);
    }

    public IchigoKurosaki()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(__ ->
        {
            ForcePower force = GameUtilities.GetPower(player, ForcePower.class);
            if (force != null && force.GetCurrentLevel() > 2)
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