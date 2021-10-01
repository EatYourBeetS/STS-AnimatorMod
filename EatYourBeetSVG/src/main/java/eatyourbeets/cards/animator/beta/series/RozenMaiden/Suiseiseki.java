package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.GameActions;

public class Suiseiseki extends AnimatorCard // TODO
{
    public static final EYBCardData DATA = Register(Suiseiseki.class)
    		.SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Slimed(), false));

    public Suiseiseki()
    {
        super(DATA);

        Initialize(0, 6, 4, 3);
        SetUpgrade(0, 2, 1);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Orange(2, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryHP(magicNumber);

        GameActions.Bottom.MakeCard(new Status_Slimed(), player.drawPile);

        if (info.IsSynergizing && CombatStats.TryActivateSemiLimited(cardID))
        {
            String stance = player.stance.ID;

            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);

            if (stance.equals(ForceStance.STANCE_ID))
            {
                GameActions.Bottom.GainForce(secondaryValue);
            }
            else if (stance.equals(AgilityStance.STANCE_ID))
            {
                GameActions.Bottom.GainAgility(secondaryValue);
            }
            else if (stance.equals(IntellectStance.STANCE_ID))
            {
                GameActions.Bottom.GainIntellect(secondaryValue);
            }
            else if (stance.equals(WillpowerStance.STANCE_ID))
            {
                GameActions.Bottom.GainWillpower(secondaryValue);
            }
        }
    }
}

// [A][Block] 4
// Cycle 1 card or exit your {Stance}.
// (On Discard or ){Starter}: Add a random card to your discard pile, 
// with rarity same as the card discarded, then gain [Agi].

