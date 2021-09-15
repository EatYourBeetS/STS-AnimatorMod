package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_ApplyToAll;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.PlayerAttribute;
import eatyourbeets.utilities.TargetHelper;

public class IsshinKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IsshinKurosaki.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsshinKurosaki()
    {
        super(DATA);

        Initialize(0, 6, 1, 2);
        SetUpgrade(0, 3, 0);
        SetAffinity_Red(2, 0, 0);

        SetAffinityRequirement(Affinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        makeChoice(m);

        if (CheckAffinity(Affinity.Red) && CombatStats.TryActivateLimited(cardID))
        {
            makeChoice(m);
        }
    }

    private void makeChoice(AbstractMonster m) {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_ApplyToAll(TargetHelper.Enemies(), PowerHelper.Burning, magicNumber));
            choices.AddEffect(new GenericEffect_GainStat(secondaryValue, PlayerAttribute.Force));
        }
        choices.Select(1, m);
    }
}