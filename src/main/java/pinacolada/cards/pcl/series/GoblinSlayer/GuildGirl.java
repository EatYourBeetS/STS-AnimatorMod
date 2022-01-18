package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnEnemyDyingSubscriber;
import pinacolada.actions.player.GainGold;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GuildGirl extends PCLCard
{
    public static final PCLCardData DATA = Register(GuildGirl.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMultiformData(2)
            .SetSeriesFromClassPackage();
    public static final int GOLD_GAIN = 4;

    public GuildGirl()
    {
        super(DATA);

        Initialize(0, 0, GOLD_GAIN);

        SetAffinity_Orange(1);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new GuildGirlPower(p, 1));
    }

    public static class GuildGirlPower extends PCLPower implements OnEnemyDyingSubscriber
    {
        public GuildGirlPower(AbstractCreature owner, int amount)
        {
            super(owner, GuildGirl.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onEnemyDying.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onEnemyDying.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            PCLActions.Bottom.Callback(() -> PCLActions.Bottom.Cycle(name, amount));
            this.flash();
        }

        @Override
        public void OnEnemyDying(AbstractMonster monster, boolean triggerRelics)
        {
            if (PCLGameUtilities.IsFatal(monster, false))
            {
                PCLActions.Top.Add(new GainGold(GOLD_GAIN, true));
                this.flash();
            }
        }
    }
}