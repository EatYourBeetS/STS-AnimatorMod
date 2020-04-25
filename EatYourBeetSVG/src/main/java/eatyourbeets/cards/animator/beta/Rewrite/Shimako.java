package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Shimako extends AnimatorCard {
    public static final EYBCardData DATA = Register(Shimako.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Shimako() {
        super(DATA);

        Initialize(0, 3, 0);
        SetUpgrade(0, 3, 0);
        SetScaling(1,0,0);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        GameActions.Bottom.GainBlock(block);

        String curStance = player.stance.ID;
        AbstractOrb orb;

        if (curStance.equals(ForceStance.STANCE_ID))
        {
            orb = new Fire();
        }
        else if (curStance.equals(AgilityStance.STANCE_ID))
        {
            orb = new Frost();
        }
        else if (curStance.equals(IntellectStance.STANCE_ID))
        {
            orb = new Dark();
        }
        else
        {
            orb = new Lightning();
        }

        GameActions.Bottom.ChannelOrb(orb, true);
    }
}