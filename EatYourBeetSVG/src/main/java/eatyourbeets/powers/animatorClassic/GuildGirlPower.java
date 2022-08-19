package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import eatyourbeets.actions.player.GainGold;
import eatyourbeets.interfaces.subscribers.OnEnemyDyingSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class GuildGirlPower extends AnimatorPower implements OnEnemyDyingSubscriber
{
    public static final String POWER_ID = CreateFullID(GuildGirlPower.class);
    public static final int GOLD_GAIN = 4;

    public GuildGirlPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onEnemyDying.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onEnemyDying.Unsubscribe(this);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.Callback(() -> GameActions.Bottom.Cycle(name, amount));
    }

    @Override
    public void OnEnemyDying(AbstractMonster monster, boolean triggerRelics)
    {
        if (!monster.hasPower(MinionPower.POWER_ID) && !monster.hasPower(RegrowPower.POWER_ID))
        {
            GameActions.Top.Add(new GainGold(GOLD_GAIN, true));
            this.flash();
        }
    }

//    @Override
//    public void onVictory()
//    {
//        super.onVictory();
//
//        AbstractRoom room = GameUtilities.GetCurrentRoom();
//        if (room != null && room.rewardAllowed && goldReward > 0)
//        {
//            room.addGoldToRewards(goldReward);
//
//            //The following can't be used because, for UNKNOWN REASONS it changes the rng of the other rewards
//            //room.rewards.add(new SpecialGoldReward(GuildGirl.DATA.Strings.NAME, goldReward));
//        }
//    }
}