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

        Initialize(0, 5, 1, 2);
        SetUpgrade(0, 2, 0, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        GameActions.Bottom.GainBlock(block);

        if (IsStarter() && HasTeamwork(secondaryValue))
        {
            AbstractOrb firstOrb = null;
            for (AbstractOrb orb : p.orbs)
            {
                if (Earth.ORB_ID.equals(orb.ID))
                {
                    firstOrb = orb;
                    break;
                }
            }

            if (firstOrb != null) {
                firstOrb.onStartOfTurn();
                firstOrb.onEndOfTurn();
            }
            else
            {
                GameActions.Bottom.ChannelOrb(new Earth());
            }
        }

        if (HasSynergy())
        {
            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseBlock += c.magicNumber);
        }
    }
}