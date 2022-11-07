package eatyourbeets.cards.animatorClassic.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class SilverFang extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(SilverFang.class).SetSeriesFromClassPackage().SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public SilverFang()
    {
        super(DATA);

        Initialize(0, 9, 1);
        SetUpgrade(0, 3, 0);

        
        SetMartialArtist();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (AgilityStance.IsActive())
        {
            GameActions.Bottom.SelectFromHand(name, 1, true)
            .SetFilter(c -> c instanceof EYBCard && c.type == CardType.ATTACK)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    EYBCard card = (EYBCard)cards.get(0);
                    GameActions.Bottom.IncreaseScaling(card, Affinity.Green, 1);
                    card.flash();
                }
            });
        }

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        }
    }
}