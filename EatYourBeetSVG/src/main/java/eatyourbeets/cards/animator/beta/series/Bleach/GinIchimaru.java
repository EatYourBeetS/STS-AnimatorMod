package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.PlayerAttribute;

public class GinIchimaru extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(GinIchimaru.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public GinIchimaru()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Orange(2, 0, 0);
        SetAffinity_Green(1, 0, 0);

        SetExhaust(true);
        SetAffinityRequirement(Affinity.General, 4);
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