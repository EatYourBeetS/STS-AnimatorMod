package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;

public class Kuroyukihime extends AnimatorCard
{
    public static final String ID = Register(Kuroyukihime.class.getSimpleName());

    public Kuroyukihime()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.AccelWorld);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new BlackLotus(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .AddCallback(__ ->
        {
            GameActions.Bottom.MakeCardInHand(new BlackLotus(), false, false);
            GameActions.Bottom.Exhaust(this);
        });
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}