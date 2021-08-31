package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.PlayerAttribute;
import eatyourbeets.utilities.TargetHelper;

public class GinIchimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GinIchimaru.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Piercing, EYBCardTarget.None).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public GinIchimaru()
    {
        super(DATA);

        Initialize(2, 0, 2, 0);
        SetUpgrade(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(2, 0, 2);

        SetAffinityRequirement(Affinity.Red, 2);
        SetAffinityRequirement(Affinity.Green, 2);
        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Green, 3);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new DieDieDieEffect());
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }

        if (CheckAffinity(Affinity.Red) || CheckAffinity(Affinity.Green))
        {
            GameActions.Bottom.Exhaust(this);
        }

        if (CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        makeChoice(m, CheckAffinity(Affinity.General) && CombatStats.TryActivateLimited(cardID) ? 2 : 1);
    }

    private void makeChoice(AbstractMonster m, int selections) {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_GainStat(GetHandAffinity(Affinity.Red), PlayerAttribute.Force));
            choices.AddEffect(new GenericEffect_GainStat(GetHandAffinity(Affinity.Green), PlayerAttribute.Agility));
        }
        choices.Select(selections, m);
    }
}