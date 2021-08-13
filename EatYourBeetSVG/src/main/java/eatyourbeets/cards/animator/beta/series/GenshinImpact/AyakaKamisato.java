package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.SheerCold;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.NegateBlockPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class AyakaKamisato extends AnimatorCard {
    public static final EYBCardData DATA = Register(AyakaKamisato.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing).SetSeriesFromClassPackage();
    private static final int ATTACK_TIMES = 2;
    static
    {
        DATA.AddPreview(new SheerCold(), false);
    }

    public AyakaKamisato() {
        super(DATA);

        Initialize(13, 0, 3, 1);
        SetUpgrade(2, 0, 2, 0);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(Affinity.Blue, 4);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(ATTACK_TIMES);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        for (int i = 0; i < ATTACK_TIMES; i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, i % 2 == 0 ? AttackEffects.SLASH_VERTICAL : AttackEffects.SLASH_DIAGONAL)
                    .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.TEAL, Color.LIGHT_GRAY, Color.SKY, Color.BLUE).duration * 0.6f)
                    .SetOptions(true, false);
        }
        GameActions.Bottom.GainTemporaryThorns(magicNumber + CombatStats.Affinities.GetPowerAmount(Affinity.Blue));
        GameActions.Bottom.StackPower(new NegateBlockPower(p, ATTACK_TIMES));

        if (CheckAffinity(Affinity.Blue) && CombatStats.TryActivateLimited(cardID))
        {
            AbstractCard c = new SheerCold();
            c.applyPowers();
            c.use(player, null);
        }
    }
}