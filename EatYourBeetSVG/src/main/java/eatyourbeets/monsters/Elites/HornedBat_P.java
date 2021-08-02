package eatyourbeets.monsters.Elites;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.replacement.TemporaryConfusionPower;
import eatyourbeets.powers.monsters.HornedBat_PurplePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HornedBat_P extends HornedBat
{
    public static final String ID = CreateFullID(HornedBat_P.class);

    protected final EYBAbstractMove confusionMove;
    protected final EYBAbstractMove strengthLossMove;

    public HornedBat_P(float x, float y)
    {
        super(new Data(ID), x, y);

        confusionMove = moveset.Special.ShuffleCard(new Dazed(), 1)
        .SetOnSelect((m) -> TurnData.Get().UsedConfusion = true)
        .SetCanUse((m, b) -> m.CanUseFallback(b) && !TurnData.Get().UsedConfusion)
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(this, new TemporaryConfusionPower(t)));

        strengthLossMove = moveset.Special.StrongDebuff(PowerHelper.Strength, -1)
        .SetOnSelect((m) -> TurnData.Get().UsedStrengthLoss = true)
        .SetCanUse((m, b) -> m.CanUseFallback(b) && (!TurnData.Get().UsedStrengthLoss &&
        GameUtilities.GetPowerAmount(AbstractDungeon.player, StrengthPower.POWER_ID) >= 0));

        moveset.SetFindSpecialMove((roll) ->
        {
            if (moveHistory.size() >= 1 && strengthLossMove.CanUse(previousMove))
            {
                return strengthLossMove;
            }
            else if (roll < 25 && confusionMove.CanUse(previousMove))
            {
                return confusionMove;
            }

            return null;
        });

        //Rotation:
        moveset.Normal.ShuffleCard(new Dazed(), 1)
        .SkipAnimation(true)
        .SetIntent(Intent.DEFEND_DEBUFF)
        .SetMiscBonus(17, 1)
        .SetBlock(3);

        moveset.Normal.AttackDebuff(3, PowerHelper.Frail, 1)
        .SetDamageBonus(3, 1)
        .SetMiscBonus(8, 1);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(new HornedBat_PurplePower(this));
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            if (GameUtilities.GetAscensionLevel() >= 8)
            {
                SetMaxHP(28, 32);
            }
            else
            {
                SetMaxHP(24, 28);
            }

            atlasUrl = "images/animator/monsters/HornedBat/HornedBat_P.atlas";
            jsonUrl = "images/animator/monsters/HornedBat/HornedBat.json";
            scale = 1f;

            SetHB(0, 0, 120, 150, 0, 50);
        }
    }
}
