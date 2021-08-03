package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.utilities.GameActions;

public class CerealBox extends AnimatorRelic
{
    public static final String ID = CreateFullID(CerealBox.class);
    public static final int HEAL_AMOUNT = 6;
    public static final int HEAL_COST = 2;

    public CerealBox()
    {
        super(ID, RelicTier.SHOP, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, HEAL_COST, HEAL_AMOUNT);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        GameActions.Bottom.ApplyPower(new CerealBoxPower(player, this));
    }

    public static class CerealBoxPower extends AnimatorClickablePower
    {
        public CerealBoxPower(AbstractCreature owner, EYBRelic relic)
        {
            super(owner, relic, PowerTriggerConditionType.Energy, CerealBox.HEAL_COST);

            this.triggerCondition.SetUses(1, false, false);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, CerealBox.HEAL_AMOUNT);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.Heal(CerealBox.HEAL_AMOUNT);
        }
    }
}