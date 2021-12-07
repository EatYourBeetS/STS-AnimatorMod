package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Millim extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Millim.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Millim()
    {
        super(DATA);

        Initialize(6, 0, 5, 2);

        SetAffinity_Star(1, 0, 1);

        SetAffinityRequirement(Affinity.General, 5);
        SetUnique(true, true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + GameUtilities.GetDebuffsCount(enemy) * magicNumber);
    }


    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 1)
        {
            upgradeMagicNumber(1);
            upgradeDamage(1);
        }
        else
        {
            upgradeMagicNumber(0);
            upgradeDamage(2);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);

        GameActions.Bottom.StackPower(TargetHelper.Normal(m), GameUtilities.GetRandomElement(GameUtilities.GetCommonDebuffs()), secondaryValue)
                .ShowEffect(true, true);
        GameActions.Bottom.StackPower(TargetHelper.Normal(m), GameUtilities.GetRandomElement(GameUtilities.GetCommonDebuffs()), secondaryValue)
                .ShowEffect(true, true);


        GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.GainRandomAffinityPower(1, false);
            }
        });
    }
}