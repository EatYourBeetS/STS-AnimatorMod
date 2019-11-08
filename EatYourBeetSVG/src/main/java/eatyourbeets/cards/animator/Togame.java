package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.VariableExhaustAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Togame extends AnimatorCard
{
    public static final String ID = Register(Togame.class.getSimpleName(), EYBCardBadge.Special);

    public Togame()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.AddToBottom(new VariableExhaustAction(p, 1, this, this::OnExhaust));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private void OnExhaust(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards.size() > 0)
        {
            GameActionsHelper.DrawCard(AbstractDungeon.player, 1);

            AbstractCard card = cards.get(0);
            if ((card.type == CardType.CURSE || card.type == CardType.STATUS) && PlayerStatistics.TryActivateSemiLimited(cardID))
            {
                GameActionsHelper.Motivate(1);
            }
            //GameActionsHelper.ExhaustCard(this);
            //GameActionsHelper.GainEnergy(1);
        }
    }
}