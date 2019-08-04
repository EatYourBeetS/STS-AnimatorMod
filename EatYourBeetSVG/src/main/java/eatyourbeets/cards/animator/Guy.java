package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.DiscardTopCardsAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.ArrayList;

public class Guy extends AnimatorCard
{
    public static final String ID = CreateFullID(Guy.class.getSimpleName());

    public Guy()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 1);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.Discard(this.magicNumber, false);
        //GameActionsHelper.AddToBottom(new VariableDiscardAction(p, this.magicNumber, this, this::OnDiscard, false));

        if (HasActiveSynergy())
        {
            GameActionsHelper.AddToBottom(new DiscardTopCardsAction(p.drawPile, 2));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private void OnDiscard(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards != null && cards.size() > 0)
        {
            for (AbstractCard card : cards)
            {
                if (card.type == CardType.ATTACK)
                {
                    GameActionsHelper.GainBlock(AbstractDungeon.player, this.block);
                    return;
                }
            }
        }
    }
}