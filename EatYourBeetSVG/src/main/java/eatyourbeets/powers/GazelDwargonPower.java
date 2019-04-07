package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.relics.Calipers;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.subscribers.OnBeforeLoseBlockSubscriber;

public class GazelDwargonPower extends AnimatorPower implements OnBeforeLoseBlockSubscriber
{
    public static final String POWER_ID = CreateFullID(GazelDwargonPower.class.getSimpleName());

    public GazelDwargonPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        PlayerStatistics.onBeforeLoseBlock.Subscribe(this);

        updateDescription();
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PlayerStatistics.onBeforeLoseBlock.Unsubscribe(this);
    }

    @Override
    public void OnBeforeLoseBlock(AbstractCreature creature, int blockAmount, boolean noAnimation)
    {
        if (creature.isPlayer && noAnimation)
        {
            int blockLost = Math.min(creature.currentBlock, blockAmount);
            if (blockLost > 0)
            {
                for (int i = 0; i < this.amount; i++)
                {
                    for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
                    {
                        GameActionsHelper.DamageTarget(creature, m, blockLost, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                    }
                }
            }
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        AbstractPlayer p = AbstractDungeon.player;
        if ((!p.hasPower(BarricadePower.POWER_ID)) && (!p.hasPower(BlurPower.POWER_ID)))
        {
            if (!p.hasRelic(Calipers.ID))
            {
                p.loseBlock(true);
            }
            else
            {
                p.loseBlock(15, true);
            }
        }
    }
}
