package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class AyaShameimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AyaShameimaru.class).SetPower(2, CardRarity.RARE).SetSeriesFromClassPackage();

    public AyaShameimaru()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);
        SetAffinity_Green(2, 0, 0);
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
        GameActions.Bottom.ChannelOrb(new Air());
        GameActions.Bottom.StackPower(new AyaShameimaruPower(p, magicNumber));
    }

    public static class AyaShameimaruPower extends AnimatorPower implements OnOrbApplyFocusSubscriber
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

            CombatStats.onOrbApplyFocus.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onOrbApplyFocus.Unsubscribe(this);
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {

            super.onEvokeOrb(orb);

            if (Air.ORB_ID.equals(orb.ID)) {
                CombatStats.Affinities.AddAffinity(CombatStats.Affinities.GetAffinityLevel(Affinity.Green,true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Orange,true) ? Affinity.Orange : Affinity.Green, amount);
            }
        }

        @Override
        public void OnApplyFocus(AbstractOrb orb) {
            int index = player.orbs.indexOf(orb);
            if (index >= 0 && !Air.ORB_ID.equals(orb.ID)) {
                if (index + 1 < player.filledOrbCount() && Air.ORB_ID.equals(player.orbs.get(index + 1).ID)) {
                    orb.passiveAmount += amount;
                    orb.evokeAmount += amount;
                }
                if (index - 1 >= 0 && Air.ORB_ID.equals(player.orbs.get(index - 1).ID)) {
                    orb.passiveAmount += amount;
                    orb.evokeAmount += amount;
                }
            }
        }
    }
}

