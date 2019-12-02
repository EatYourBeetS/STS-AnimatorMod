package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.ChooseFromAnyPileAction;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class CowGirl extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(CowGirl.class.getSimpleName(), EYBCardBadge.Discard);

    public CowGirl()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper.Motivate(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DelayedAction(this);
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }

    private CardGroup GetZeroCost(CardGroup source)
    {
        CardGroup result = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : source.group)
        {
            if (c.freeToPlayOnce || c.costForTurn == 0)
            {
                result.group.add(c);
            }
        }

        return result;
    }

    private void OnCardSelected(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards != null && cards.size() > 0)
        {
            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(cards.get(0), AbstractDungeon.player.hand, true));
        }
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            AbstractPlayer p = AbstractDungeon.player;
            if (upgraded)
            {
                GameActionsHelper.AddToBottom(new ChooseFromAnyPileAction(1, this::OnCardSelected, this, FetchAction.TEXT[0], GetZeroCost(p.drawPile), GetZeroCost(p.discardPile)));
            }
            else
            {
                GameActionsHelper.AddToBottom(new ChooseFromAnyPileAction(1, this::OnCardSelected, this, FetchAction.TEXT[0], GetZeroCost(p.drawPile)));
            }
        }
    }
}