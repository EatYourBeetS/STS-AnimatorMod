package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Sora_BattlePlan3 extends Sora_BattlePlan
{
    public static final EYBCardData DATA = Register(Sora_BattlePlan3.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetImagePath(IMAGE_PATH)
            .SetSeries(SERIES);

    public Sora_BattlePlan3()
    {
        super(DATA);

        Initialize(0, 0, 2);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ObtainAffinityToken(Affinity.General, false).Repeat(magicNumber);

        for (Affinity a : Affinity.Basic())
        {
            GameActions.Bottom.StackAffinityPower(a, 1, true);
        }
    }
}