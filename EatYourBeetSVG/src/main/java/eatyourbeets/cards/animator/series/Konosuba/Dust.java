package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Dust extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Dust.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Dust()
    {
        super(DATA);

        Initialize(7, 0, 2, 1);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1);

        SetAffinityRequirement(Affinity.Fire, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this,m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(1).SetFilter(c -> c.rarity.equals(CardRarity.BASIC) || c.rarity.equals(CardRarity.COMMON), false);

        if (IsStarter() && CheckAffinity(Affinity.Fire)) {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Normal(m), magicNumber);
        };
    }
}