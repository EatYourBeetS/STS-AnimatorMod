package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.utilities.PCLActions;

public class Haniwa extends PCLCard
{
    public static final PCLCardData DATA = Register(Haniwa.class).SetSkill(1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None);

    public Haniwa()
    {
        super(DATA);

        Initialize(0, 6, 0);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        PCLActions.Bottom.Callback(this::RefreshCost);
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
            if (c != this && Haniwa.DATA.ID.equals(c.cardID))
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
        PCLActions.Bottom.GainBlock(block);
    }
}