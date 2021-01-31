package eatyourbeets.cards.animator.beta.Bleach;

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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                int cost = cards.get(0).costForTurn;

                if (cost >= 3)
                {
                    int numEmptyOrbs = 0;

                    for (AbstractOrb orb : player.orbs)
                    {
                        if (orb instanceof EmptyOrbSlot)
                        {
                            numEmptyOrbs++;
                        }
                    }

                    for (int i = 0; i < numEmptyOrbs; i++)
                    {
                        GameActions.Bottom.ChannelOrb(new Fire(), true);
                    }
                }
                else if (cost == 2)
                {
                    GameActions.Bottom.GainTemporaryHP(magicNumber);
                }
                else
                {
                    GameActions.Bottom.GainBlock(secondaryValue);
                }
            }
        });
    }
}