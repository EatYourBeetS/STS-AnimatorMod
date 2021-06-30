package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Noelle extends AnimatorCard {
    public static final EYBCardData DATA = Register(Noelle.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self);

    public Noelle() {
        super(DATA);

        Initialize(0, 2, 1);
        SetUpgrade(0, 1, 1);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (magicNumber * GameUtilities.GetTeamwork(null));
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            boolean hasEarth = false;
            for (AbstractOrb orb : p.orbs)
                if (Earth.ORB_ID.equals(orb.ID)) {
                    hasEarth = true;
                    break;
                }

            if (!hasEarth)
                GameActions.Bottom.ChannelOrb(new Earth());
        }
    }
}