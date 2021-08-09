package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Noelle extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noelle.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public Noelle()
    {
        super(DATA);

        Initialize(0, 5, 1);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 1, 0);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(Affinity.Orange, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
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

        if (CheckAffinity(Affinity.Orange) && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.SelectFromHand(name, magicNumber, false)
                    .SetOptions(true, true, true)
                    .SetMessage(RetainCardsAction.TEXT[0])
                    .SetFilter(c -> !c.isEthereal)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            AbstractCard card = cards.get(0);
                            GameUtilities.Retain(card);
                        }
                    });
        }
    }
}