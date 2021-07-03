package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Noelle extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noelle.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self);

    public Noelle()
    {
        super(DATA);

        Initialize(0, 5, 2, 2);
        SetUpgrade(0, 2, 0, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            boolean hasEarth = false;
            for (AbstractOrb orb : p.orbs)
            {
                if (Earth.ORB_ID.equals(orb.ID))
                {
                    hasEarth = true;
                    break;
                }
            }

            if (!hasEarth)
            {
                GameActions.Bottom.ChannelOrb(new Earth());
            }
        }

        if (HasSynergy() && HasTeamwork(secondaryValue))
        {
            GameActions.Bottom.SelectFromHand(name, 1, true)
                    .SetFilter(c -> c instanceof EYBCard && c.block > 0)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            EYBCard card = (EYBCard) cards.get(0);
                            GameActions.Bottom.ModifyAllInstances(card.uuid, c -> c.baseBlock += c.magicNumber);
                            card.flash();
                        }
                        else
                        {
                            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseBlock += c.magicNumber);
                        }
                    });
        }
    }
}