package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.BlackLotus;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Kuroyukihime extends AnimatorCard
{
    public static final String ID = Register(Kuroyukihime.class);
    static
    {
        GetStaticData(ID).InitializePreview(new BlackLotus(), false);
    }

    public Kuroyukihime()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

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