package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

public class Veldora extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Veldora.class.getSimpleName());

    public Veldora()
    {
        super(ID, 4, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 4);

        baseSecondaryValue = secondaryValue = 1;

        SetSynergy(Synergies.TenSura, true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActionsHelper.ChannelOrb(AbstractOrb.getRandomOrb(true), true);
        GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new FocusPower(p, this.secondaryValue), this.secondaryValue);

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
            upgradeSecondaryValue(1);
            upgradeMagicNumber(1);
        }
    }

    private class VeldoraAction extends AnimatorAction
    {
        private AbstractOrb orb;

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