package pinacolada.relics.pcl.replacement;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.relics.PCLRelic;
import pinacolada.relics.PCLReplacementRelic;
import pinacolada.utilities.PCLActions;

public class CerealBox extends PCLReplacementRelic
{
    public static final String ID = CreateFullID(CerealBox.class);
    public static final int HEAL_COST = 1;
    public static final int HEAL_AMOUNT = 3;
    public static final int BASE_USES = 5;
    public static final int SHOP_BONUS_USES = 3;

    public CerealBox()
    {
        super(ID, eatyourbeets.relics.animator.CerealBox.ID, RelicTier.SHOP, LandingSound.FLAT);

        SetCounter(BASE_USES);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, HEAL_COST, HEAL_AMOUNT, BASE_USES, SHOP_BONUS_USES);
    }

    @Override
    public void justEnteredRoom(AbstractRoom room)
    {
        super.justEnteredRoom(room);

        if (room instanceof ShopRoom)
        {
            AddCounter(SHOP_BONUS_USES);
            flash();
        }
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        if (counter > 0)
        {
            PCLActions.Bottom.ApplyPower(new CerealBoxPower(player, this));
        }
    }

    public static class CerealBoxPower extends PCLClickablePower
    {
        private final PCLRelic relic;

        public CerealBoxPower(AbstractCreature owner, PCLRelic relic)
        {
            super(owner, relic, PowerTriggerConditionType.Energy, CerealBox.HEAL_COST);

            this.triggerCondition.SetUses(relic.counter, false, false);
            this.relic = relic;
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(1, triggerCondition.requiredAmount, CerealBox.HEAL_AMOUNT);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            if (this.relic.counter > 0)
            {
                this.relic.AddCounter(-1);
                PCLActions.Bottom.Heal(CerealBox.HEAL_AMOUNT);
            }

            this.triggerCondition.uses = relic.counter;
        }
    }
}