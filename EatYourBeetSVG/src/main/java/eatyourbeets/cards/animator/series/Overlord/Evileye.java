package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_Draw;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_GainOrBoost;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_GainOrbSlots;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class Evileye extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Evileye.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int INTELLECT_AMOUNT = 3;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Evileye()
    {
        super(DATA);

        Initialize(0,0,4, 2);

        SetAffinity_Blue(1, 1, 0);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Dark, 4);
        SetAffinityRequirement(Affinity.Light, 4);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(INTELLECT_AMOUNT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_GainOrBoost(Affinity.Blue, INTELLECT_AMOUNT, false));
        choices.AddEffect(new GenericEffect_GainOrbSlots(secondaryValue));
        choices.AddEffect(new GenericEffect_Draw(magicNumber));
        choices.Select(1, m);

        if (CheckAffinity(Affinity.Dark) || CheckAffinity(Affinity.Light))
        {
            GameActions.Bottom.GainEnergy(1);
            this.exhaustOnUseOnce = true;
        }
    }
}