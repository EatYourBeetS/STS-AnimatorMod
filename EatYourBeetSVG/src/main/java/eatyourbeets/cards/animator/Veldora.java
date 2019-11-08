package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

public class Veldora extends AnimatorCard_UltraRare
{
    public static final String ID = Register(Veldora.class.getSimpleName(), EYBCardBadge.Drawn);

    public Veldora()
    {
        super(ID, 4, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 2, 2);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActionsHelper.ChannelOrb(AbstractOrb.getRandomOrb(true), true);
        GameActionsHelper.ChannelRandomOrb(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainIntellect(secondaryValue);

        int orbCount =  p.filledOrbCount();
        for (int i = 0; i < this.magicNumber - 1; i++)
        {
            for (AbstractOrb orb : p.orbs)
            {
                if (!(orb instanceof EmptyOrbSlot))
                {
                    GameActionsHelper.AddToBottom(new VeldoraAction(orb));
                }
            }
        }

        GameActionsHelper.AddToBottom(new AnimateOrbAction(orbCount));
        GameActionsHelper.AddToBottom(new EvokeOrbAction(orbCount));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private class VeldoraAction extends AnimatorAction
    {
        private final AbstractOrb orb;

        public VeldoraAction(AbstractOrb orb)
        {
            this.orb = orb;
        }

        @Override
        public void update()
        {
            orb.triggerEvokeAnimation();
            orb.onEvoke();

            this.isDone = true;
        }
    }
}