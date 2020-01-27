package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Sebas extends AnimatorCard
{
    public static final String ID = Register(Sebas.class, EYBCardBadge.Discard);

    public Sebas()
    {
        super(ID, 2, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 7, 2, 3);
        SetUpgrade(0, 4, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainTemporaryHP(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            if (GameUtilities.IsAttacking(enemy.intent))
            {
                GameActions.Bottom.GainBlock(block);
                GameActions.Bottom.GainThorns(magicNumber);
            }
        }
    }
}