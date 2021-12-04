package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NobleFencer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NobleFencer.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public NobleFencer()
    {
        super(DATA);

        Initialize(0, 3, 3, 1);

        SetAffinity_Green(2);
        SetAffinity_Blue(2);
        SetAffinity_Red(0,0,1);

        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        final AbstractOrb orb = GameUtilities.GetFirstOrb(Lightning.ORB_ID);
        if (orb != null)
        {
            orb.showEvokeValue();
        }
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.GainWisdom(secondaryValue);
        }

        GameActions.Bottom.EvokeOrb(1).SetFilter(Lightning.class::isInstance);
        GameActions.Bottom.StackPower(new NobleFencerPower(p, magicNumber));
    }

    public class NobleFencerPower extends AnimatorPower implements OnSynergySubscriber {
        public NobleFencerPower(AbstractCreature owner, int amount) {
            super(owner, NobleFencer.DATA);
            this.Initialize(amount);
        }

        public void onInitialApplication() {
            super.onInitialApplication();
            CombatStats.onSynergy.Subscribe(this);
        }

        public void onRemove() {
            super.onRemove();
            CombatStats.onSynergy.Unsubscribe(this);
        }

        public void OnSynergy(AbstractCard card) {
            GameActions.Bottom.ChannelOrb(new Lightning());
            this.ReducePower(1);
        }

        public void atEndOfTurn(boolean isPlayer) {
            super.atEndOfTurn(isPlayer);
            this.RemovePower();
        }
    }
}