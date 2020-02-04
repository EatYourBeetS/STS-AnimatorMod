package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Gluttony extends AnimatorCard
{
    public static final String ID = Register_Old(Gluttony.class);

    public Gluttony()
    {
        super(ID, 2, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 4, 16);
        SetCostUpgrade(-1);

        SetHealing(true);
        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);

        if (playable)
        {
            int total = player.drawPile.size() + player.discardPile.size() + player.hand.size();
            if (total < secondaryValue)
            {
                cantUseMessage = cardData.Strings.EXTENDED_DESCRIPTION[0];

                return false;
            }
        }

        return playable && (player.drawPile.size() >= magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.drawPile.size() >= magicNumber)
        {
            GameActions.Top.MoveCards(p.drawPile, p.exhaustPile, magicNumber)
            .ShowEffect(true, true)
            .SetOptions(false, true);

            GameActions.Bottom.Heal(magicNumber);
            GameActions.Bottom.GainForce(magicNumber);
        }
    }
}