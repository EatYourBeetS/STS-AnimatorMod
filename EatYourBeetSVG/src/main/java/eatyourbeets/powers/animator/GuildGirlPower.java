package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.animator.series.GoblinSlayer.GuildGirl;
import eatyourbeets.interfaces.subscribers.OnEnemyDyingSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.rewards.animator.SpecialGoldReward;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GuildGirlPower extends AnimatorPower implements OnEnemyDyingSubscriber
{
    public static final String POWER_ID = CreateFullID(GuildGirlPower.class.getSimpleName());

    private final String rewardName;
    private final int goldGain;

    private int goldReward;

    public GuildGirlPower(AbstractCreature owner, int amount, int goldGain)
    {
        super(owner, POWER_ID);

        this.rewardName = GuildGirl.DATA.Strings.NAME;
        this.amount = amount;
        this.goldReward = 0;
        this.goldGain = goldGain;

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
            this.goldReward += this.goldGain;
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        AbstractRoom room = GameUtilities.GetCurrentRoom();
        if (room != null && room.rewardAllowed && goldReward > 0)
        {
            room.rewards.add(0, new SpecialGoldReward(rewardName, goldReward));
        }
    }
}