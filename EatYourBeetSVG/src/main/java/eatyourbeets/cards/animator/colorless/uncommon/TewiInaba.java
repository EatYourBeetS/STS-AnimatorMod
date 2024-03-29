package eatyourbeets.cards.animator.colorless.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class TewiInaba extends AnimatorCard implements Hidden // TODO:
{
    public static final EYBCardData DATA = Register(TewiInaba.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public TewiInaba()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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
