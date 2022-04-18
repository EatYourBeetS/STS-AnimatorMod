package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.ui.cards.CardPreview;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class DolaRiku extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaRiku.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private final CardPreview discardPilePreview;
    private final CardPreview drawPilePreview;

    public DolaRiku()
    {
        super(DATA);

        Initialize(0, 6);
        SetUpgrade(0, 3);

        SetAffinity_Light(1);
        SetAffinity_Green(1);

        discardPilePreview = SetCardPreview(c -> c.type == CardType.ATTACK)
        .SetSelection(CardSelection.Top, 1)
        .SetGroupType(CardGroup.CardGroupType.DISCARD_PILE);

        drawPilePreview = SetCardPreview(c -> c.type == CardType.ATTACK);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        cardPreview = HasSynergy() ? discardPilePreview : drawPilePreview;
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(true, true, false)
        .SetFilter(c -> c.type == CardType.SKILL)
        .AddCallback(info, (info2, cards) ->
        {
            if (cards.size() >= 1)
            {
                if (info2.IsSynergizing)
                {
                    GameActions.Bottom.FetchFromPile(name, 1, player.discardPile)
                    .ShowEffect(true, false)
                    .SetOptions(CardSelection.Top, true)
                    .SetFilter(c -> c.type == CardType.ATTACK);
                }
                else
                {
                    GameActions.Bottom.Draw(1).SetFilter(c -> c.type == CardType.ATTACK, false);
                }
            }
        });
    }
}