package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shinoa extends AnimatorCard
{
    public static final String ID = Register(Shinoa.class);

    public Shinoa()
    {
        super(ID, 1, CardRarity.COMMON, CardType.SKILL, CardTarget.ALL);

        Initialize(0, 6, 1);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyWeak(player, enemy, magicNumber);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);

        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyVulnerable(player, enemy, magicNumber);

            if (HasSynergy())
            {
                GameActions.Bottom.ApplyWeak(player, enemy, magicNumber);
            }
        }
    }
}