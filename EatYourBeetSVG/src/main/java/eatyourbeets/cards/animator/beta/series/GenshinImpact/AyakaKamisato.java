package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.SheerCold;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SelfImmolationPower;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class AyakaKamisato extends AnimatorCard {
    public static final EYBCardData DATA = Register(AyakaKamisato.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing).SetSeriesFromClassPackage()
            .SetMaxCopies(2)
            .PostInitialize(data -> data.AddPreview(new SheerCold(), false));

    public AyakaKamisato() {
        super(DATA);

        Initialize(18, 0, 2, 5);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Blue(2, 0, 3);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Dark(0, 0, 3);

        SetAffinityRequirement(Affinity.Blue, 6);

        SetEthereal(true);
        SetExhaust(true);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL)
                .forEach(d -> d.SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.TEAL, Color.LIGHT_GRAY, Color.SKY, Color.BLUE).duration * 0.1f));
        GameActions.Bottom.StackPower(new SelfImmolationPower(p, magicNumber));

        if (CombatStats.CanActivateLimited(cardID) && (GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID) >= secondaryValue || TrySpendAffinity(Affinity.Blue)) && CombatStats.TryActivateLimited(cardID))
        {
            AbstractCard c = new SheerCold();
            c.applyPowers();
            c.use(player, null);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return (CombatStats.CanActivateLimited(cardID) && CheckAffinity(Affinity.Blue) || GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID) >= secondaryValue);
    }
}