package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.animator.GuildGirl;
import eatyourbeets.rewards.SpecialGoldReward;
import eatyourbeets.interfaces.OnEnemyDyingSubscriber;
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

        this.rewardName =  Resources_Animator.GetCardStrings(GuildGirl.ID).NAME.replace("'", "");
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

        GameActionsHelper_Legacy.AddToBottom(new GuildGirlAction(this.amount));
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

    private class GuildGirlAction extends AbstractGameAction
    {
        public GuildGirlAction(int amount)
        {
            this.amount = amount;
        }

        @Override
        public void update()
        {
            GameActionsHelper_Legacy.CycleCardAction(this.amount, name);

            isDone = true;
        }
    }
}
