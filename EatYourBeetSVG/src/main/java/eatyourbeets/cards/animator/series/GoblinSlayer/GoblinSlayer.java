package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameUtilities;

public class GoblinSlayer extends AnimatorCard
{
    public static final String ID = Register(GoblinSlayer.class.getSimpleName(), EYBCardBadge.Special);

    public GoblinSlayer()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(7,0);

        SetRetain(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        int turnCount = PlayerStatistics.getTurnCount();
        if (turnCount % 2 == 1)
        {
            int goblins = 1;
            if (turnCount > 3)
            {
                goblins += 1;
            }
            if (turnCount > 7)
            {
                goblins += 1;
            }

            GameActions.Bottom.Add(new CreateRandomGoblins(goblins));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        SetRetain(true);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (player.exhaustPile.size() * 3));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        MoveCards(p.discardPile, p.exhaustPile);
        MoveCards(p.hand, p.exhaustPile);

        GameActions.Top.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }

    private void MoveCards(CardGroup source, CardGroup destination)
    {
        float duration = 0.3f;

        for (AbstractCard card : source.group)
        {
            if (GameUtilities.IsCurseOrStatus(card))
            {
                GameActions.Top.MoveCard(card, destination, source)
                .ShowEffect(true, true, duration = Math.max(0.1f, duration * 0.8f))
                .SetCardPosition(MoveCard.DEFAULT_CARD_X_RIGHT, MoveCard.DEFAULT_CARD_Y);
            }
        }
    }
}