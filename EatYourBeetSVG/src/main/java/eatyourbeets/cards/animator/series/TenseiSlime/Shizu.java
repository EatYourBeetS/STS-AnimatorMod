package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.animator.special.Shizu_Ifrit;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Shizu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shizu.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Shizu_Ifrit(), true));

    public Shizu()
    {
        super(DATA);

        Initialize(14, 0, 2, 3);
        SetUpgrade(3, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1);
        SetAffinity_Light(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE)
        .SetDamageEffect(c -> GameEffects.List.Attack(player, c, AttackEffects.SLASH_DIAGONAL, 0.9f, 1.1f).duration * 0.33f);
        GameActions.Bottom.StackPower(new FlamingWeaponPower(p, 2));

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.MakeCardInHand(new Shizu_Ifrit()).SetUpgrade(upgraded, false);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        if (CombatStats.CanActivateLimited(cardID) && (GameUtilities.GetOrbCount(Dark.ORB_ID) >= 1) && (GameUtilities.GetOrbCount(Fire.ORB_ID) >= 1))
        {
            return !tryUse || CombatStats.TryActivateLimited(cardID);
        }

        return false;
    }
}