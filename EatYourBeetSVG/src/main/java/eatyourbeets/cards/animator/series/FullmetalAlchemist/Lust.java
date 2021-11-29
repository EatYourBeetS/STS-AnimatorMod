package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Lust extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lust.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Lust()
    {
        super(DATA);

        Initialize(0, 0, 5, 2);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Dark(1, 1, 0);
        SetAffinity_Orange(1, 0, 0);

        SetAffinityRequirement(Affinity.Dark, 5);
    }

    @Override
    public void OnUpgrade() {
        SetRetainOnce(true);
        SetAffinityRequirement(Affinity.Dark, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int amount = TrySpendAffinity(Affinity.Dark) ? secondaryValue + 1 : secondaryValue;
        GameActions.Top.ApplyFrail(TargetHelper.Enemies(), amount);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), amount);
        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), amount);
        GameActions.Bottom.DealDamageAtEndOfTurn(p, p, magicNumber, AttackEffects.SLASH_VERTICAL);
    }
}