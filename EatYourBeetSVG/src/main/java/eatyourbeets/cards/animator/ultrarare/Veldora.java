package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Veldora extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Veldora.class).SetSkill(4, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Veldora()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);

        showEvokeValue = true;

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.ChannelOrb(AbstractOrb.getRandomOrb(true));
        GameActions.Bottom.ChannelRandomOrbs(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainIntellect(secondaryValue);

        int orbCount = p.filledOrbCount();
        for (int i = 0; i < magicNumber - 1; i++)
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