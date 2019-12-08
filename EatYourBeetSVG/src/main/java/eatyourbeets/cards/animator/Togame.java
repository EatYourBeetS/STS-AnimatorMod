package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.actions._legacy.common.VariableExhaustAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

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
        GameActionsHelper2.Draw(this.magicNumber);
        GameActionsHelper.AddToBottom(new VariableExhaustAction(name, 1, this, this::OnExhaust));
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
            if (GameUtilities.IsCurseOrStatus(card) && EffectHistory.TryActivateSemiLimited(cardID))
            {
                GameActionsHelper.Motivate(1);
            }
        }
    }
}