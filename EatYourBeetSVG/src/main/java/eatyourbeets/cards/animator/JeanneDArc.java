package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class JeanneDArc extends AnimatorCard_UltraRare implements StartupCard, OnCallbackSubscriber
{
    public static final String ID = Register(JeanneDArc.class.getSimpleName(), EYBCardBadge.Special);

    public JeanneDArc()
    {
        super(ID, 1, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(12,4, 8);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper2.GainBlock(block);

        if (HasActiveSynergy())
        {
            GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);
            GameActionsHelper.DelayedAction(this);
            GameActionsHelper.ResetOrder();
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    private boolean TryExhaust(CardGroup source)
    {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : source.group)
        {
            if (GameUtilities.IsCurseOrStatus(c))
            {
                cards.add(c);
            }
        }

        if (cards.size() > 0)
        {
            GameActionsHelper.AddToTop(new ExhaustSpecificCardAction(JavaUtilities.GetRandomElement(cards), source));

            return true;
        }

        return false;
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActionsHelper.GainTemporaryHP(AbstractDungeon.player, magicNumber);

        return true;
    }


    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            AbstractPlayer p = AbstractDungeon.player;
            if (!TryExhaust(p.drawPile))
            {
                if (!TryExhaust(p.hand))
                {
                    TryExhaust(p.discardPile);
                }
            }
        }
    }
}