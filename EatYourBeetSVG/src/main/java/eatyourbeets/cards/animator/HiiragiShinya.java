package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.RefreshHandLayoutAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class HiiragiShinya extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(HiiragiShinya.class.getSimpleName(), EYBCardBadge.Synergy);

    public HiiragiShinya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,4, 2);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        String message = MoveCardsAction.TEXT[0] + " (" + name + ")";

        GameActionsHelper2.GainBlock(block);
        GameActionsHelper.ChooseFromPile(1, false, p.discardPile, this::OnCompletion, this, message, true);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, magicNumber), magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }

    private void OnCompletion(Object state, ArrayList<AbstractCard> cards)
    {
        if (state.equals(this) && cards != null && cards.size() == 1)
        {
            AbstractPlayer p = AbstractDungeon.player;
            AbstractCard c = cards.get(0);

            GameActionsHelper.MoveCard(c, p.hand, p.discardPile, true);
            GameActionsHelper.DelayedAction(this, c);
        }
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        AbstractCard c = JavaUtilities.SafeCast(state, AbstractCard.class);
        if (c != null)
        {
            c.setCostForTurn(c.costForTurn + 1);
            c.retain = true;
            GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
        }
    }
}