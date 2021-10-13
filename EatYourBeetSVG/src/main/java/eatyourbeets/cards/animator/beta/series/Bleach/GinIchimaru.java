package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.PlayerAttribute;
import eatyourbeets.utilities.TargetHelper;

public class GinIchimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GinIchimaru.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Piercing, EYBCardTarget.Random).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public GinIchimaru()
    {
        super(DATA);

        Initialize(3, 0, 2, 1);
        SetUpgrade(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Earth(1, 0, 0);
        SetAffinity_Air(2, 0, 2);

        SetAffinityRequirement(Affinity.Fire, 3);
        SetAffinityRequirement(Affinity.Air, 3);
        SetAffinityRequirement(Affinity.Water, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinityRequirement(Affinity.Fire, 4);
        SetAffinityRequirement(Affinity.Air, 4);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new DieDieDieEffect());
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }

        if (CheckAffinity(Affinity.Fire) || CheckAffinity(Affinity.Air))
        {
            GameActions.Bottom.Exhaust(this);
        }

        if (CheckAffinity(Affinity.Water))
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() -> makeChoice(m, 1));
    }

    private void makeChoice(AbstractMonster m, int selections) {
        if (choices.TryInitialize(this))
        {
            if (GetLevelAffinity(Affinity.Fire) > 0) {
                choices.AddEffect(new GenericEffect_GainStat(GetLevelAffinity(Affinity.Fire), PlayerAttribute.Force));
            }
            if (GetLevelAffinity(Affinity.Air) > 0) {
                choices.AddEffect(new GenericEffect_GainStat(GetLevelAffinity(Affinity.Air), PlayerAttribute.Agility));
            }
        }
        choices.Select(selections, m);
    }
}