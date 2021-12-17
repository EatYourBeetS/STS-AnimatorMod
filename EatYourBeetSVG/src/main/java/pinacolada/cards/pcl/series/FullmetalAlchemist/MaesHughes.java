package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class MaesHughes extends PCLCard
{
    public static final PCLCardData DATA = Register(MaesHughes.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeries(CardSeries.FullmetalAlchemist);

    public MaesHughes()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, -1);

        SetAffinity_Orange(1);
        SetAffinity_Light(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.Callback(() ->
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(Math.floorDiv(p.drawPile.size(), magicNumber));
        PCLActions.Bottom.Motivate();
    }

    private boolean DrawRoyMustang(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (RoyMustang.DATA.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    PCLGameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    PCLActions.Top.MoveCard(c, group, player.hand)
                    .ShowEffect(true, true)
                    .AddCallback(card -> PCLActions.Bottom.Motivate(card, 1));
                }

                return true;
            }
        }

        return false;
    }
}