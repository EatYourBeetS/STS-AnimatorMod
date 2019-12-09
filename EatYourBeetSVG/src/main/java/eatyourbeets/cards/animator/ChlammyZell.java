package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.ChlammyZellPower;

public class ChlammyZell extends AnimatorCard
{
    public static final String ID = Register(ChlammyZell.class.getSimpleName(), EYBCardBadge.Special);

    public ChlammyZell()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(0, 0, 4, 1);

        SetExhaust(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.GainIntellect(secondaryValue);
        GameActions.Bottom.StackPower(new ChlammyZellPower(p, this.magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}