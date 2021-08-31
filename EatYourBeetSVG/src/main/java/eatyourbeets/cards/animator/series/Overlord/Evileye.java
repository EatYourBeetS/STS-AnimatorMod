package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_Draw;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainOrBoost;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainOrbSlots;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class Evileye extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Evileye.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Evileye()
    {
        super(DATA);

        Initialize(0,0, 3, 2);

        SetAffinity_Blue(2);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Dark, 4);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_GainOrBoost(GR.Tooltips.Intellect, magicNumber, false));
        choices.AddEffect(new GenericEffect_GainOrbSlots(secondaryValue));
        choices.AddEffect(new GenericEffect_Draw(magicNumber));
        choices.Select(1, m);

        if (CheckAffinity(Affinity.Dark))
        {
            GameActions.Bottom.GainEnergy(1);
            this.exhaustOnUseOnce = true;
        }
    }
}