package eatyourbeets.cards_beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.utilities.GameActions;

public class MayumiJoutouguu_Haniwa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MayumiJoutouguu_Haniwa.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public MayumiJoutouguu_Haniwa()
    {
        super(DATA);

        Initialize(0, 6, 0);
        SetUpgrade(0, 3, 0);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        GameActions.Bottom.Callback(this::RefreshCost);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    public void RefreshCost()
    {
        boolean otherHaniwaInHand = false;
        for (AbstractCard c : player.hand.group)
        {
            if (c != this && MayumiJoutouguu_Haniwa.DATA.ID.equals(c.cardID))
            {
                otherHaniwaInHand = true;
                break;
            }
        }

        if (otherHaniwaInHand)
        {
            CostModifiers.For(this).Set(-1);
        }
        else
        {
            CostModifiers.For(this).Set(0);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }
}