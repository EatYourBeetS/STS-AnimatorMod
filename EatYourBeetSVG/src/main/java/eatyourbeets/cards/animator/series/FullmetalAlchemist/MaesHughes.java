package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class MaesHughes extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MaesHughes.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public MaesHughes()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, -1);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(() ->
        {
            if (!DrawRoyMustang(player.drawPile))
            {
                if (!DrawRoyMustang(player.discardPile))
                {
                    if (!DrawRoyMustang(player.exhaustPile))
                    {
                        DrawRoyMustang(player.hand);
                    }
                }
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(Math.floorDiv(p.drawPile.size(), magicNumber));
        GameActions.Bottom.Motivate();
    }

    private boolean DrawRoyMustang(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (RoyMustang.DATA.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    GameActions.Top.MoveCard(c, group, player.hand)
                    .ShowEffect(true, true)
                    .AddCallback(card -> card.setCostForTurn(card.costForTurn-1));
                }

                return true;
            }
        }

        return false;
    }
}