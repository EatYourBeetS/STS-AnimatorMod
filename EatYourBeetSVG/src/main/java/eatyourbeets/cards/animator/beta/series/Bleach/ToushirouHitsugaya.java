package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class ToushirouHitsugaya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ToushirouHitsugaya.class).SetAttack(1, CardRarity.RARE, EYBAttackType.Normal).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ToushirouHitsugaya_Bankai(), false));
    public static final int FREEZING_THRESHOLD = 8;

    public ToushirouHitsugaya() {
        super(DATA);

        Initialize(6, 3, 2, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Red(1,0,1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyFreezing(TargetHelper.Normal(m), magicNumber).ShowEffect(true, true).AddCallback(m, (enemy, __) -> {
            int freezingAmount = magicNumber;
            if (GameUtilities.GetPowerAmount(m, BurningPower.POWER_ID) > 0) {
                GameActions.Bottom.ReducePower(player, enemy, BurningPower.POWER_ID, secondaryValue);
                GameActions.Bottom.ApplyFreezing(TargetHelper.Normal(enemy), magicNumber).ShowEffect(true, true);
            }
        });

        GameActions.Bottom.Callback(m, (enemy, __) -> {
            if (GameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) > FREEZING_THRESHOLD) {
                GameActions.Bottom.MakeCardInDrawPile(new ToushirouHitsugaya_Bankai());
                GameActions.Bottom.Exhaust(this);
            }
        });
    }
}