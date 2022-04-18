package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TukaLunaMarceau.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public TukaLunaMarceau()
    {
        super(DATA);

        Initialize(0, 2, 1);
        SetUpgrade(0, 2, 0);

        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Draw(1);
        }

        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .SetFilter(AffinityToken.class::isInstance)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                if (c instanceof AffinityToken)
                {
                    GameUtilities.Retain(c);
                    BlockModifiers.For(c).Add(cardID, magicNumber);
                    c.superFlash();
                }
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return player.currentBlock <= 0;
    }
}