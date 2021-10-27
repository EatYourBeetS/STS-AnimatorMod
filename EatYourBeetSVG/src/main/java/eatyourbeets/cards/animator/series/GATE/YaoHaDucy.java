package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YaoHaDucy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YaoHaDucy.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();
    public YaoHaDucy()
    {
        super(DATA);

        Initialize(1, 0);
        SetUpgrade(3, 0);

        SetAffinity_Dark();
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        int amountToAdd = (int)Math.floor(GameUtilities.GetPowerAmount(SupportDamagePower.POWER_ID) / 2f);

        return super.ModifyDamage(enemy, amount + amountToAdd);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
    }
}