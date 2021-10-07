package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.stances.CorruptionStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ciel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ciel.class)
            .SetSkill(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Lu(), false));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Ciel()
    {
        super(DATA);

        Initialize(0, 4, 6, 2);
        SetUpgrade(0, 1, 2, 0);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(2);

        SetAffinityRequirement(Affinity.Blue, 4);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyLockOn(p,m,secondaryValue);

        GameActions.Bottom.ModifyAllCopies(Lu.DATA.ID)
        .AddCallback(c ->
        {
            GameUtilities.IncreaseDamage(c, magicNumber, false);
            c.flash();
        });

        if (CheckAffinity(Affinity.Blue) || (info.IsSynergizing && info.GetPreviousCardID().equals(Lu.DATA.ID)))
        {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(CorruptionStance.STANCE_ID));
            }
            choices.Select(1, m);
        }
    }
}