package eatyourbeets.cards.animator.beta.LogHorizon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.MotivateTargetAction;
import eatyourbeets.actions.cardManipulation.TempReduceBlockAction;
import eatyourbeets.actions.cardManipulation.TempReduceDamageAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Marielle extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Marielle.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Marielle()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, -1);
        SetEthereal(true);
        SetExhaust(true);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
        {
            GameActions.Bottom.Add(new MotivateTargetAction(c));
            if (c.baseBlock > 0)
            {
                GameActions.Bottom.Add(new TempReduceBlockAction(c, magicNumber));
            }

            if (c.baseDamage > 0)
            {
                GameActions.Bottom.Add(new TempReduceDamageAction(c, magicNumber));
            }

            c.flash(Color.RED.cpy());
        }

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Aether(), true);
        }
    }
}