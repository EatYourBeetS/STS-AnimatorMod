package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.List;

public class Aisha extends AnimatorCard
{
    public static final String ID = CreateFullID(Aisha.class.getSimpleName());

    public Aisha()
    {
        super(ID, 1, AbstractCard.CardType.SKILL, CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChooseAndDiscard(1, false);
        GameActionsHelper.AddToBottom(new FetchAction(p.drawPile, this::Filter, 1, this::OnFetch));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private void OnFetch(List<AbstractCard> cards)
    {
        if (cards != null && cards.size() > 0)
        {
            for (AbstractCard c : cards)
            {
                GameActionsHelper.AddToBottom(new ReduceCostAction(c.uuid, this.magicNumber));
            }
        }
    }

    private boolean Filter(AbstractCard card)
    {
        return card != null && card.type == CardType.SKILL;
    }
}