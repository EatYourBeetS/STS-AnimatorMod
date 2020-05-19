package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.AlternateCardCostModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.cards.animator.beta.AngelBeats.EriShiina;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class AfterLifeMod extends AbstractCardModifier implements AlternateCardCostModifier
{

    public static final String ID = GR.Animator.CreateID("Afterlife");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GR.Animator.CreateID("CardMods"));
    public static final String[] TEXT = uiStrings.TEXT;

    //This variable currently exists solely for Shiina
    public static int mostRecentEnergySpentByAfterlife = 0;

    public AbstractCardModifier makeCopy()
    {
        return new AfterLifeMod();
    }

    @Override
    public String identifier(AbstractCard card)
    {
        return ID;
    }

    @Override
    public int getAlternateResource(AbstractCard card)
    {
        return AbstractDungeon.player.exhaustPile.size();
    }

    @Override
    public boolean canSplitCost(AbstractCard card)
    {
        return true;
    }

    @Override
    public int spendAlternateCost(AbstractCard card, int costToSpend)
    {
        int resource = -1;
        if (AbstractDungeon.player.exhaustPile.size() > 0)
        {
            resource = AbstractDungeon.player.exhaustPile.size();
        }
        if (resource > costToSpend)
        {
            for (int i = 0; i < costToSpend; i++)
            {
                AbstractDungeon.player.exhaustPile.group.remove(AbstractDungeon.cardRandomRng.random(AbstractDungeon.player.exhaustPile.size() - 1));
            }
            mostRecentEnergySpentByAfterlife = costToSpend;
            costToSpend = 0;
        }
        else if (resource > 0)
        {
            for (int i = 0; i < resource; i++)
            {
                AbstractDungeon.player.exhaustPile.group.remove(AbstractDungeon.cardRandomRng.random(AbstractDungeon.player.exhaustPile.size() - 1));
            }
            mostRecentEnergySpentByAfterlife = resource;
            costToSpend -= resource;
        }
        if (card.cardID.equals(EriShiina.DATA.ID))
        {
            GameActions.Bottom.CreateThrowingKnives(mostRecentEnergySpentByAfterlife);
        }
        mostRecentEnergySpentByAfterlife = 0;
        System.out.println(CardModifierManager.getModifiers(card, ID));
        return costToSpend;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card)
    {
        return ((card instanceof AnimatorCard) ? TEXT[0] : TEXT[1]) + rawDescription;
    }
}
