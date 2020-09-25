package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class AyaShameimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AyaShameimaru.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self);

    public AyaShameimaru()
    {
        super(DATA);

        Initialize(0, 5, 1, 2);
        SetUpgrade(0, 3, 0, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.ModifyAllInstances(uuid, c ->
        {
            c.baseBlock += secondaryValue;
            c.applyPowers();
        });
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        if (HasSynergy())
        {
            if (GameUtilities.InStance(NeutralStance.STANCE_ID))
            {
                GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
            }
        }
    }
}

