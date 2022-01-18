package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class NobleFencer extends PCLCard
{
    public static final PCLCardData DATA = Register(NobleFencer.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public NobleFencer()
    {
        super(DATA);

        Initialize(0, 3, 3, 1);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);
        SetAffinity_Red(0,0,1);

        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        final AbstractOrb orb = PCLGameUtilities.GetFirstOrb(Lightning.ORB_ID);
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
        PCLActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            PCLActions.Bottom.GainWisdom(secondaryValue);
        }

        PCLActions.Bottom.EvokeOrb(1).SetFilter(Lightning.class::isInstance);
        PCLActions.Bottom.StackPower(new NobleFencerPower(p, magicNumber));
    }

    public static class NobleFencerPower extends PCLPower implements OnSynergySubscriber {
        public NobleFencerPower(AbstractCreature owner, int amount) {
            super(owner, NobleFencer.DATA);
            this.Initialize(amount);
        }

        public void onInitialApplication() {
            super.onInitialApplication();
            PCLCombatStats.onSynergy.Subscribe(this);
        }

        public void onRemove() {
            super.onRemove();
            PCLCombatStats.onSynergy.Unsubscribe(this);
        }

        public void OnSynergy(AbstractCard card) {
            PCLActions.Bottom.ChannelOrb(new Lightning());
            this.ReducePower(1);
        }

        public void atEndOfTurn(boolean isPlayer) {
            super.atEndOfTurn(isPlayer);
            this.RemovePower();
        }
    }
}