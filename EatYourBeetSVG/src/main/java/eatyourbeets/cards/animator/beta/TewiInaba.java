package eatyourbeets.cards.animator.beta;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class TewiInaba extends AnimatorCard implements Hidden // TODO:
{
    public static final EYBCardData DATA = Register(TewiInaba.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);

    public TewiInaba()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.orbs.size() > 0)
        {
            AbstractOrb orb = p.orbs.get(0);
            if (!EmptyOrbSlot.ORB_ID.equals(orb.ID))
            {
                GameActions.Bottom.Add(new EvokeSpecificOrbAction(orb));
                GameActions.Bottom.Draw(magicNumber);

                if (Earth.ORB_ID.equals(orb.ID))
                {
                    GameActions.Bottom.GainBlock(secondaryValue);
                }
            }
        }

        GameActions.Bottom.MakeCardInDrawPile(new Dazed());
    }
}
