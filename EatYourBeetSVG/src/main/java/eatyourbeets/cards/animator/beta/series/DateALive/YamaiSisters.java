package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.affinity.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YamaiSisters extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YamaiSisters.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();

    public YamaiSisters()
    {
        super(DATA);

        Initialize(2, 0);
        SetUpgrade(1, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 1, 0);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        int forceAmount = GameUtilities.GetPowerAmount(AbstractDungeon.player, ForcePower.POWER_ID);

        if (enemy != null && GameUtilities.IsAttacking(enemy.intent) && forceAmount > 0)
        {
            damage += forceAmount;
        }

        return super.ModifyDamage(enemy, damage);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (IsStarter())
        {
            GameActions.Bottom.MakeCardInHand(makeStatEquivalentCopy());
        }
    }
}