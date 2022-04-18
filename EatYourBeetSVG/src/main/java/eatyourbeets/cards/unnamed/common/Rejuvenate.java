package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Rejuvenate extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Rejuvenate.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.Minion);

    public Rejuvenate()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 3, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.HealDoll(m, magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.Queue.ShowCopy(this);
            GameActions.Bottom.HealPlayerLimited(this, secondaryValue);
        }
    }
}