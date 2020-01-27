package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class MaesHughes extends AnimatorCard
{
    public static final String ID = Register(MaesHughes.class, EYBCardBadge.Exhaust);

    public MaesHughes()
    {
        super(ID, 1, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, -1);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(__ ->
        {
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
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(Math.floorDiv(p.drawPile.size(), magicNumber));
        GameActions.Bottom.Motivate();
    }

    private boolean DrawRoyMustang(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (RoyMustang.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    GameActions.Top.MoveCard(c, group, AbstractDungeon.player.hand)
                    .ShowEffect(true, true)
                    .AddCallback(card -> card.setCostForTurn(card.costForTurn-1));
                }

                return true;
            }
        }

        return false;
    }
}