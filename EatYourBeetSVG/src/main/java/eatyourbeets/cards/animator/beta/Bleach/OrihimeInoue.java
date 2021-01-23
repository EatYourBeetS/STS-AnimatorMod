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
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrihimeInoue extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrihimeInoue.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public OrihimeInoue()
    {
        super(DATA);

        Initialize(0, 0, 5, 8);
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
    public AbstractAttribute GetSpecialInfo()
    {
        if (GameUtilities.InBattle()) {

            AbstractCard topCard = null;

            if (player.discardPile.size() > 0)
            {
                topCard = player.discardPile.getBottomCard();
            }

            if (topCard != null)
            {
                int cost = topCard.costForTurn;

                if (cost == 2)
                {
                    return TempHPAttribute.Instance.SetCard(this, true);
                }
            }
        }

        return null;
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        AbstractCard topCard = null;

        if (player.discardPile.size() > 0)
        {
            topCard = player.discardPile.getBottomCard();
        }

        if (topCard != null)
        {
            int cost = topCard.costForTurn;

            if (cost < 2)
            {
                super.ModifyBlock(enemy, amount + secondaryValue);
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
            topCard = p.discardPile.getBottomCard();
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
                GameActions.Bottom.GainTemporaryHP(magicNumber);
            }
            else
            {
                GameActions.Bottom.GainBlock(secondaryValue);
            }
        }
    }
}