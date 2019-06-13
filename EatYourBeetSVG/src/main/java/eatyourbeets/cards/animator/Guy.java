package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Guy extends AnimatorCard
{
    public static final String ID = CreateFullID(Guy.class.getSimpleName());

    public Guy()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,3, 1);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.AddToBottom(new VariableDiscardAction(p, this.magicNumber, this, this::OnDiscard, false));
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