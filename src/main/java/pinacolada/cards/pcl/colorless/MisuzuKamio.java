package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.actions.orbs.EvokeOrb;
import pinacolada.cards.base.*;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.orbs.pcl.Air;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class MisuzuKamio extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(MisuzuKamio.class)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Air);

    public MisuzuKamio()
    {
        super(DATA);

        Initialize(0, 1, 1, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Light(1, 0, 4);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetHaste(true);
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        if (CheckSpecialCondition(true)) {
            PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Air, player.maxOrbs);
            PCLCombatStats.onStartOfTurnPostDraw.Subscribe((MisuzuKamio) this.makeStatEquivalentCopy());
        }
        else {
            PCLActions.Bottom.GainInvocation(magicNumber);
        }
        if (info.CanActivateLimited) {
            boolean trigger = false;
            for (AbstractPower po : player.powers) {
                if (po.canGoNegative && po.amount < 0) {
                    PCLActions.Bottom.StackPower(player, po, MathUtils.floor(po.amount / -2f));
                    trigger = true;
                }
            }
            if (trigger) {
                info.TryActivateLimited();
            }
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        PCLGameEffects.Queue.ShowCardBriefly(this);
        PCLActions.Bottom.EvokeOrb(player.filledOrbCount(), EvokeOrb.Mode.Sequential)
                .SetFilter(o -> Air.ORB_ID.equals(o.ID));
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLGameUtilities.IsPCLAffinityPowerActive(PCLAffinity.Light);
    }
}