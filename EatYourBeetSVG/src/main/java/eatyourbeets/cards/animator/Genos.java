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
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.BurningPower;

import java.util.ArrayList;

public class Genos extends AnimatorCard
{
    public static final String ID = Register(Genos.class.getSimpleName(), EYBCardBadge.Synergy);

    public Genos()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(6, 0, 2, 4);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new SelfDamagePower(p, secondaryValue), secondaryValue);

        if (HasActiveSynergy() && PlayerStatistics.TryActivateSemiLimited(cardID))
        {
            GameActionsHelper.AddToBottom(new ChooseFromAnyPileAction(1, this::OnCardSelected, this,
                                            FetchAction.TEXT[0], GetHighCost(p.drawPile), GetHighCost(p.discardPile)));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }

    private CardGroup GetHighCost(CardGroup source)
    {
        CardGroup result = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : source.group)
        {
            if (c.costForTurn >= 3)
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
            AbstractCard card = cards.get(0);
            card.retain = true;
            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(card, AbstractDungeon.player.hand));
        }
    }
}