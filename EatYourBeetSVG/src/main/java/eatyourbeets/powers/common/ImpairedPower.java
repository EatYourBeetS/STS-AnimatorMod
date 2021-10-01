package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.UUID;

public class ImpairedPower extends CommonPower implements OnOrbApplyFocusSubscriber
{
    public static final String POWER_ID = CreateFullID(ImpairedPower.class);
    private static UUID battleID;
    public static final int ORB_MULTIPLIER = 50;
    public static int PLAYER_MODIFIER = 0;

    public static void AddPlayerModifier(int multiplier)
    {
        InitializeForBattle();
        PLAYER_MODIFIER += multiplier;

        for (ImpairedPower p : GameUtilities.<ImpairedPower>GetPowers(TargetHelper.Enemies(), POWER_ID))
        {
            p.updateDescription();
            p.flashWithoutSound();
        }
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

    private static void InitializeForBattle() {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            PLAYER_MODIFIER = 0;
        }
    }

    public static int GetOrbMultiplier()
    {
        return (ORB_MULTIPLIER + PLAYER_MODIFIER);
    }

    public ImpairedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        ReducePower(1);
    }

    @Override
    public void updateDescription() {
        this.description = FormatDescription(0,GetOrbMultiplier(),amount,amount == 1 ? powerStrings.DESCRIPTIONS[1] : powerStrings.DESCRIPTIONS[2]);
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        orb.passiveAmount *= Math.max(0,GetOrbMultiplier() / 100f);
        orb.evokeAmount *= Math.max(0,GetOrbMultiplier() / 100f);
    }
}