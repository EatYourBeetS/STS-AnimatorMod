package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Animate extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Animate.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.Minion);

    public Animate()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ActivateDoll(m, 1);

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Draw(1);
        }
    }
}