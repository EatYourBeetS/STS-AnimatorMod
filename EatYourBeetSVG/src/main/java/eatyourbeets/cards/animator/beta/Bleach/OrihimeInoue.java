package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class OrihimeInoue extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrihimeInoue.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public OrihimeInoue()
    {
        super(DATA);

        Initialize(0, 0, 8, 2);
        SetUpgrade(0, 0, 0);
        SetExhaust(true);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        AbstractCard topCard = null;

        if (player.discardPile.size() > 0)
        {
            topCard = player.discardPile.getTopCard();
        }

        if (topCard != null)
        {
            int cost = topCard.costForTurn;

            if (cost < 2)
            {
                super.ModifyBlock(enemy, amount + magicNumber);
            }
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractCard topCard = null;

        if (p.discardPile.size() > 0)
        {
            topCard = p.discardPile.getTopCard();
        }

        if (topCard != null)
        {
            int cost = topCard.costForTurn;

            if (costForTurn >= 3)
            {
                int numEmptyOrbs = 0;

                for (AbstractOrb orb : player.orbs)
                {
                    if (orb instanceof EmptyOrbSlot)
                    {
                        numEmptyOrbs++;
                    }
                }

                for (int i=0; i<numEmptyOrbs; i++)
                {
                    GameActions.Bottom.ChannelOrb(new Fire(), true);
                }
            }
            else if (costForTurn == 2)
            {
                for (int i=0; i<secondaryValue; i++)
                {
                    AbstractCard cardToDraw = null;

                    if (p.discardPile.size() > 0)
                    {
                        cardToDraw = p.discardPile.getTopCard();
                    }

                    if (cardToDraw != null)
                    {
                        GameActions.Bottom.Draw(cardToDraw);
                    }
                }
            }
            else
            {
                GameActions.Bottom.GainBlock(magicNumber);
            }
        }
    }
}