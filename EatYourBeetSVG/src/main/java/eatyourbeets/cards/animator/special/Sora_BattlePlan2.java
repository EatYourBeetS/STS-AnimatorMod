package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Sora_BattlePlan2 extends Sora_BattlePlan
{
    public static final EYBCardData DATA = Register(Sora_BattlePlan2.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetImagePath(IMAGE_PATH)
            .SetSeries(SERIES);

    public Sora_BattlePlan2()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
        GameActions.Bottom.GainTemporaryStats(0, secondaryValue, 0);
    }
}