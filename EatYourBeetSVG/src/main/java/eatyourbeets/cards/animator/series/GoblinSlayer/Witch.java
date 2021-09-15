package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Witch extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Witch.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Spearman(), true));

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Witch()
    {
        super(DATA);

        Initialize(0, 11);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return Spearman.DATA.ID.equals(other.cardID) || super.HasDirectSynergy(other);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (upgraded)
        {
            GameActions.Bottom.TriggerOrbPassive(p.orbs.size())
            .SetFilter(o -> Dark.ORB_ID.equals(o.ID) || Fire.ORB_ID.equals(o.ID))
            .SetSequential(true);
        }

        if (CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }
        if (CheckAffinity(Affinity.Dark))
        {
            GameActions.Bottom.ChannelOrb(new Dark());
        }

        if (info.IsSynergizing && info.GetPreviousCardID().equals(Spearman.DATA.ID))
        {
            if (choices.TryInitialize(new Spearman()))
            {
                choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
                choices.Initialize(this);
                choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(NeutralStance.STANCE_ID));
            }

            choices.Select(1, m);
        }
    }
}