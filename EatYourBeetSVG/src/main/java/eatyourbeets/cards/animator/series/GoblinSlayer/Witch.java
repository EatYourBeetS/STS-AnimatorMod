package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

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

        Initialize(0, 9);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        final AbstractOrb orb = GetLeftmostOrb();
        if (orb != null)
        {
            orb.showEvokeValue();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.GainEnergy(1);
        }

        if (Spearman.DATA.IsCard(info.PreviousCard))
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

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        final AbstractOrb orb = GetLeftmostOrb();
        if (orb == null)
        {
            return false;
        }
        else if (tryUse)
        {
            GameActions.Bottom.EvokeOrb(1, orb);
        }

        return true;
    }

    private static AbstractOrb GetLeftmostOrb()
    {
        final ArrayList<AbstractOrb> orbs = player.orbs;
        for (int i = player.maxOrbs - 1; i >= 0; i--)
        {
            AbstractOrb orb = orbs.get(i);
            if (orb != null && (Dark.ORB_ID.equals(orb.ID) || Fire.ORB_ID.equals(orb.ID)))
            {
                return orb;
            }
        }

        return null;
    }
}