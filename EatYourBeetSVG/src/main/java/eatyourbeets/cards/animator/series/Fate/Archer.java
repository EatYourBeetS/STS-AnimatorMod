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
    public static final EYBCardData DATA = Register(Archer.class).SetPower(1, CardRarity.UNCOMMON);

    public Archer()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded)
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.StackPower(new ArcherPower(p, magicNumber));

        if (HasSynergy())
        {
            for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
            {
                GameActions.Bottom.ApplyVulnerable(p, enemy, 1);
            }
        }
    }
}