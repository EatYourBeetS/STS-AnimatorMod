package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ItamiYouji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ItamiYouji.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public ItamiYouji()
    {
        super(DATA);

        Initialize(2, 0, 1, 5);
        SetUpgrade(1, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Light(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Star, 2);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        final int cardsPlayed = GameUtilities.GetTotalCardsPlayed(this, true);
        GameUtilities.ModifyMagicNumber(this, baseMagicNumber + Math.min(10, cardsPlayed), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(1.3f, 1.5f);
        }

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, secondaryValue));
        }
    }
}