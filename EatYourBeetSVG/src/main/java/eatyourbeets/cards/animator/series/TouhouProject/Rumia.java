package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Rumia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rumia.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Dark), true));

    public Rumia()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Dark(1, 1, 0);

        SetAffinityRequirement(Affinity.Dark, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.TriggerOrbPassive(1)
        .SetFilter(o -> Dark.ORB_ID.equals(o.ID))
        .AddCallback(orbs ->
        {
            if (orbs.isEmpty())
            {
                GameActions.Top.ChannelOrb(new Dark());
            }
            else
            {
                GameActions.Top.ObtainAffinityToken(Affinity.Dark, upgraded);
                GameActions.Top.SFX(SFX.ATTACK_MAGIC_FAST_1, 0.75f, 0.8f).SetDuration(0.2f, true);
            }
        });

        if (CheckAffinity(Affinity.Dark))
        {
            GameActions.Bottom.EvokeOrb(1)
            .SetFilter(o -> Dark.ORB_ID.equals(o.ID))
            .AddCallback(orbs ->
            {
                if (orbs.size() > 0)
                {
                    GameActions.Bottom.StackPower(new RumiaPower(player, orbs));
                }
            });
        }
    }

    public static class RumiaPower extends AnimatorPower
    {
        protected final ArrayList<AbstractOrb> orbs = new ArrayList<>();

        public RumiaPower(AbstractCreature owner, ArrayList<AbstractOrb> orbs)
        {
            super(owner, Rumia.DATA);

            this.orbs.addAll(orbs);

            Initialize(this.orbs.size());
        }

        @Override
        protected void OnSamePowerApplied(AbstractPower power)
        {
            super.OnSamePowerApplied(power);

            orbs.addAll(((RumiaPower) power).orbs);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            for (AbstractOrb orb : orbs)
            {
                GameActions.Bottom.ChannelOrb(orb);
            }
            RemovePower();
            flash();
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new RumiaPower(owner, orbs);
        }
    }
}