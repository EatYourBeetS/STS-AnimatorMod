package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.ArcherPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Archer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Archer.class).SetPower(1, CardRarity.COMMON);

    public Archer()
    {
        super(DATA);

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
            for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
            {
                GameActions.Bottom.ApplyVulnerable(p, enemy, 1);
            }
        }
    }
}