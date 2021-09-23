package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class SakuyaOhtori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SakuyaOhtori.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public SakuyaOhtori()
    {
        super(DATA);

        Initialize(0, 3, 2, 1);
        SetUpgrade(0, 2, 0);
        SetAffinity_Green(2, 0, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.GainBlock(block);
        }

        if (GameUtilities.InStance(ForceStance.STANCE_ID))
        {
            GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);

            Dark dark = null;

            for (AbstractOrb orb : player.orbs)
            {
                dark = JUtils.SafeCast(orb, Dark.class);
                if (JUtils.SafeCast(orb, Dark.class) != null)
                {
                    break;
                }
            }

            if (dark != null)
            {
                dark.evokeAmount *= 2;
            }
        }
        else
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);

            for (int i = 0; i < p.hand.size(); i++)
            {
                AbstractCard card = p.hand.getNCardFromTop(i);
                if (card != this && card instanceof AnimatorCard && ((AnimatorCard) card).affinities.GetLevel(Affinity.Green) > 0 && GameUtilities.Retain(card))
                {
                    card.flash();
                    return;
                }
            }
        }
    }
}