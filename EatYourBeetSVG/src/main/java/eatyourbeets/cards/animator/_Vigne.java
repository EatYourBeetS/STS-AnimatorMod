package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class _Vigne extends AnimatorCard
{
    public static final String ID = CreateFullID(_Vigne.class.getSimpleName());

    public _Vigne()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0);

        AddExtendedDescription();

        SetSynergy(Synergies.GabrielDropOut);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractCard toTransform = FindCardToReplace(p.hand);
        if (toTransform != null)
        {
            ReplaceCard(p.hand, toTransform);
            p.hand.refreshHandLayout();
            return;
        }

        toTransform = FindCardToReplace(p.drawPile);
        if (toTransform != null)
        {
            AbstractCard replacement = GetReplacement();
            p.drawPile.removeCard(toTransform);
            GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(replacement, 1, true, true));
            return;
        }

        toTransform = FindCardToReplace(p.discardPile);
        if (toTransform != null)
        {
            AbstractCard replacement = GetReplacement();
            p.discardPile.removeCard(toTransform);
            GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(replacement, 1));
        }
    }

    private AbstractCard FindCardToReplace(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (c.type == CardType.CURSE || c.type == CardType.STATUS)
            {
                return c;
            }
        }

        return null;
    }

    private void ReplaceCard(CardGroup group, AbstractCard card)
    {
        int index = group.group.indexOf(card);
        group.removeCard(card);
        AbstractCard replacement = GetReplacement();
        group.group.add(index, replacement);
        replacement.applyPowers();
    }

    private AbstractCard GetReplacement()
    {
        AbstractCard replacement = null;
        while (replacement == null || replacement.tags.contains(CardTags.HEALING))
        {
            replacement = AbstractDungeon.colorlessCardPool.getRandomCard(true).makeCopy();
        }

        if (upgraded)
        {
            replacement.upgrade();
        }

        return replacement;
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}