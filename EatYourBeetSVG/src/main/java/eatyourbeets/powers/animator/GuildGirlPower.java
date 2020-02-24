package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.interfaces.subscribers.OnEnemyDyingSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GuildGirlPower extends AnimatorPower implements OnEnemyDyingSubscriber
{
    public static final String POWER_ID = CreateFullID(GuildGirlPower.class.getSimpleName());
    public static final int GOLD_GAIN = 4;

    private int goldReward;

    public GuildGirlPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.goldReward = 0;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PlayerStatistics.onEnemyDying.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PlayerStatistics.onEnemyDying.Unsubscribe(this);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.Callback(__ -> GameActions.Bottom.Cycle(name, amount));
    }

    @Override
    public void OnEnemyDying(AbstractMonster monster, boolean triggerRelics)
    {
        if (!monster.hasPower(MinionPower.POWER_ID) && !monster.hasPower(RegrowPower.POWER_ID))
        {
            this.goldReward += GOLD_GAIN;
            this.flash();
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        AbstractRoom room = GameUtilities.GetCurrentRoom();
        if (room != null && room.rewardAllowed && goldReward > 0)
        {
            room.addGoldToRewards(goldReward);

            //The following can't be used because, for UNKNOWN REASONS it changes the rng of the other rewards
            //room.rewards.add(new SpecialGoldReward(GuildGirl.DATA.Strings.NAME, goldReward));
        }
    }
}