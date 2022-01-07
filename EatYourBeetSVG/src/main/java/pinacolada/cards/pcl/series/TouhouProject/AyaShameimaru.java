package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import pinacolada.orbs.pcl.Air;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AyaShameimaru extends PCLCard
{
    public static final PCLCardData DATA = Register(AyaShameimaru.class).SetPower(2, CardRarity.RARE).SetMaxCopies(2).SetSeriesFromClassPackage();

    public AyaShameimaru()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);

        SetEthereal(true);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();

        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrb(new Air());
        PCLActions.Bottom.StackPower(new AyaShameimaruPower(p, magicNumber));
    }

    public static class AyaShameimaruPower extends PCLPower implements OnOrbApplyFocusSubscriber
    {
        private static final CardEffectChoice choices = new CardEffectChoice();

        public AyaShameimaruPower(AbstractPlayer owner, int amount)
        {
            super(owner, AyaShameimaru.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onOrbApplyFocus.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onOrbApplyFocus.Unsubscribe(this);
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {

            super.onEvokeOrb(orb);

            if (Air.ORB_ID.equals(orb.ID)) {
                PCLActions.Bottom.AddAffinity(PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Green,true) > PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Orange,true) ? PCLAffinity.Orange : PCLAffinity.Green, amount);
            }
        }

        @Override
        public void OnApplyFocus(AbstractOrb orb) {
            int index = player.orbs.indexOf(orb);
            if (index >= 0) {
                if (index + 1 < player.filledOrbCount() && Air.ORB_ID.equals(player.orbs.get(index + 1).ID)) {
                    PCLGameUtilities.ModifyOrbTemporaryFocus(orb, amount, true, false);
                }
                if (index - 1 >= 0 && Air.ORB_ID.equals(player.orbs.get(index - 1).ID)) {
                    PCLGameUtilities.ModifyOrbTemporaryFocus(orb, amount, true, false);
                }
            }
        }


    }
}

