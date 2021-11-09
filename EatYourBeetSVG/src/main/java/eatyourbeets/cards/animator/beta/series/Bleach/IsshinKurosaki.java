package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_ApplyToAll;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.PlayerAttribute;
import eatyourbeets.utilities.TargetHelper;

public class IsshinKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IsshinKurosaki.class).SetAttack(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();
    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsshinKurosaki()
    {
        super(DATA);

        Initialize(4, 7, 2, 4);
        SetUpgrade(0, 3, 0);
        SetAffinity_Red(2, 0, 2);

        SetAffinityRequirement(Affinity.Red, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        makeChoice(m);

        if (TrySpendAffinity(Affinity.Red) || ForceStance.IsActive())
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