package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.BlackLotus;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Kuroyukihime extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuroyukihime.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new BlackLotus(), false);
    }

    public Kuroyukihime()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .AddCallback(__ ->
        {
            GameActions.Bottom.MakeCardInHand(new BlackLotus());
            GameActions.Bottom.Exhaust(this);
        });
    }
}