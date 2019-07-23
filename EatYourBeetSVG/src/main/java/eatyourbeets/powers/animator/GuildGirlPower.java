package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.cards.animator.GuildGirl;
import eatyourbeets.rewards.SpecialGoldReward;
import eatyourbeets.interfaces.OnEnemyDyingSubscriber;

public class GuildGirlPower extends AnimatorPower implements OnEnemyDyingSubscriber
{
    public static final String POWER_ID = CreateFullID(GuildGirlPower.class.getSimpleName());

    private final GuildGirl guildGirl;

    private int goldGain;
    private int goldReward;

    public GuildGirlPower(AbstractCreature owner, int amount, GuildGirl guildGirl)
    {
        super(owner, POWER_ID);

        this.guildGirl = guildGirl;
        this.amount = amount;
        this.goldReward = 0;
        this.goldGain = guildGirl.magicNumber;

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
        GameActionsHelper.AddToBottom(new GuildGirlAction(this.amount));
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

        AbstractRoom room = PlayerStatistics.GetCurrentRoom();
        if (room != null && room.rewardAllowed && goldReward > 0)
        {
            room.rewards.add(0, new SpecialGoldReward(guildGirl.originalName.replace("'", ""), goldReward));
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        GuildGirlPower other = Utilities.SafeCast(power, GuildGirlPower.class);
        if (other != null && power.owner == target)
        {
            this.goldGain += other.goldGain;
        }

        super.onApplyPower(power, target, source);
    }

    private class GuildGirlAction extends AnimatorAction
    {
        public GuildGirlAction(int amount)
        {
            this.amount = amount;
        }

        @Override
        public void update()
        {
            GameActionsHelper.CycleCardAction(this.amount);

            isDone = true;
        }
    }
}
