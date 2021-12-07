package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.animator.special.Shizu_Ifrit;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.animator.BurningWeaponPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Shizu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shizu.class)
            .SetAttack(2, CardRarity.RARE)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Shizu_Ifrit(), false));

    public Shizu()
    {
        super(DATA);

        Initialize(14, 0, 2, 2);
        SetUpgrade(2, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1);
        SetAffinity_Light(2, 0, 1);

        SetAffinityRequirement(Affinity.Red, 6);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                Initialize(14, 0, 2, 2);
                SetUpgrade(4, 0);
                AddScaling(Affinity.Red, 1);
            }
            else {
                Initialize(14, 0, 2, 2);
                SetUpgrade(0, 0, 2, 0);
            }
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new BurningWeaponPower(p, magicNumber));
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE).forEach(d -> d
        .SetDamageEffect(c -> GameEffects.List.Attack(player, c, AttackEffects.SLASH_DIAGONAL, 0.9f, 1.1f).duration));

        if (CheckSpecialCondition(true) && TrySpendAffinity(Affinity.Red))
        {
            this.exhaustOnUseOnce = true;
            GameActions.Bottom.MakeCardInHand(new Shizu_Ifrit()).AddCallback(GameUtilities::Retain);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameUtilities.GetOrbCount(Dark.ORB_ID) >= 1 && GameUtilities.GetOrbCount(Fire.ORB_ID) >= 1 && CheckAffinity(Affinity.Red);
    }
}