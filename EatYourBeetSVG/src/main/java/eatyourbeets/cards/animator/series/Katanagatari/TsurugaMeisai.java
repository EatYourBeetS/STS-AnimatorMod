package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TsurugaMeisai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TsurugaMeisai.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public TsurugaMeisai()
    {
        super(DATA);

        Initialize(0, 2, 6);
        SetUpgrade(0, 2, 0);

        SetAffinity_Light(1);
        SetAffinity_Green(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new NextTurnBlockPower(p, magicNumber));
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(true, true, true)
        .SetMessage(GR.Common.Strings.HandSelection.Copy)
        .SetFilter(c -> GameUtilities.IsLowCost(c) && c.type == CardType.ATTACK)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Bottom.MakeCardInDrawPile(GameUtilities.Imitate(c))
                .AddCallback(card ->
                {
                    if (upgraded)
                    {
                        GameUtilities.SetCardTag(card, HASTE, true);
                    }
                });
            }
        });
    }
}