package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.ArcherPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Archer extends AnimatorCard
{
    public static final String ID = Register(Archer.class, EYBCardBadge.Synergy);

    public Archer()
    {
        super(ID, 1, CardRarity.COMMON, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new ArcherPower(p, this.magicNumber));

        if (HasSynergy())
        {
            for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.ApplyVulnerable(p, enemy, 1);
            }
        }
    }
}