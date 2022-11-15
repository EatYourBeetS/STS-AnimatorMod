package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.ui.cards.CardPreview;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

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

        Initialize(0, 7);
        SetUpgrade(0, 3);

        SetAffinity_Light(1);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Blue, 2);

        discardPilePreview = SetCardPreview(c -> c.type == CardType.ATTACK)
        .SetSelection(CardSelection.Top, 1)
        .SetGroupType(CardGroup.CardGroupType.DISCARD_PILE);

        drawPilePreview = SetCardPreview(c -> c.type == CardType.ATTACK);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        cardPreview = CheckSpecialCondition(false) ? discardPilePreview : drawPilePreview;
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(true, true, false)
        .SetFilter(c -> c.type == CardType.SKILL)
        .AddCallback(cards ->
        {
            if (cards.size() >= 1)
            {
                if (CheckSpecialCondition(true))
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

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return JUtils.Any(player.discardPile.group, c -> c.type == CardType.ATTACK) && super.CheckSpecialCondition(tryUse);
    }
}