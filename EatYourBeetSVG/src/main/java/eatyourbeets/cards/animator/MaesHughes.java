package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.DrawSpecificCardAction;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActionsHelper;

public class MaesHughes extends AnimatorCard
{
    public static final String ID = Register(MaesHughes.class.getSimpleName(), EYBCardBadge.Exhaust);

    public MaesHughes()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,6, 3);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        AbstractPlayer p = AbstractDungeon.player;
        if (!DrawRoyMustang(p.drawPile))
        {
            if (!DrawRoyMustang(p.discardPile))
            {
                if (!DrawRoyMustang(p.exhaustPile))
                {
                    DrawRoyMustang(p.hand);
                }
            }
        }
    }

    private boolean DrawRoyMustang(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (RoyMustang.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameActionsHelper.AddToBottom(new MoveSpecificCardAction(c, AbstractDungeon.player.hand, group, true));
                }

                c.modifyCostForTurn(-1);

                return true;
            }
        }

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int cardDraw = Math.floorDiv(p.drawPile.size(), magicNumber);
        GameActionsHelper.DrawCard(p, cardDraw);
        GameActionsHelper.Motivate(1, 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-1);
        }
    }
}