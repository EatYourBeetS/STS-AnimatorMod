package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;

public class Veldora extends AnimatorCard_UltraRare
{
    public static final String ID = Register(Veldora.class, EYBCardBadge.Drawn);

    public Veldora()
    {
        super(ID, 4, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        showEvokeValue = true;

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.ChannelOrb(AbstractOrb.getRandomOrb(true), true);
        GameActions.Bottom.ChannelRandomOrb(true);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainIntellect(secondaryValue);

        int orbCount = p.filledOrbCount();
        for (int i = 0; i < this.magicNumber - 1; i++)
        {
            for (AbstractOrb orb : p.orbs)
            {
                if (!(orb instanceof EmptyOrbSlot))
                {
                    GameActions.Bottom.Callback(orb, (orb_, __) ->
                    {
                        ((AbstractOrb) orb_).triggerEvokeAnimation();
                        ((AbstractOrb) orb_).onEvoke();
                    });
                }
            }
        }

        GameActions.Bottom.Add(new AnimateOrbAction(orbCount));
        GameActions.Bottom.Add(new EvokeOrbAction(orbCount));
    }
}