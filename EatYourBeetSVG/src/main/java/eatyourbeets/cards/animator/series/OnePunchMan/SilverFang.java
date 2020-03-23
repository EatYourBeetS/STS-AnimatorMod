package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActions;

public class SilverFang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SilverFang.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public SilverFang()
    {
        super(DATA);

        Initialize(0, 7, 1);
        SetUpgrade(0, 1, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.OnePunchMan);
        SetMartialArtist();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainAgility(magicNumber);

        if (upgraded)
        {
            AgilityPower.PreserveOnce();
        }

        if (HasSynergy())
        {
            GameActions.Bottom.SelectFromHand(name, 1, true)
            .SetFilter(c -> c instanceof EYBCard && c.type == CardType.ATTACK)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    EYBCard card = (EYBCard)cards.get(0);
                    card.agilityScaling += 1;
                    card.flash();
                }
            });
        }
    }
}