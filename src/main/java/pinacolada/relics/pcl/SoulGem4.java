package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.powers.common.ResistancePower;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SoulGem4 extends PCLRelic
{
    public static final String ID = CreateFullID(SoulGem4.class);
    public static final int POWER_AMOUNT = 3;
    public static final int TRIGGER_THRESHOLD = 16;

    public SoulGem4()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        SetCounter(0);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();
        if (counter >= 0) {
            int oldAmount = PCLGameUtilities.GetPowerAmount(player, ResistancePower.POWER_ID);
            if (oldAmount < POWER_AMOUNT) {
                PCLActions.Bottom.GainResistance(POWER_AMOUNT - oldAmount);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (counter < 0) {
            this.usedUp();
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (counter >= 0) {
            AddCounter(1);
            flash();
            if (counter >= TRIGGER_THRESHOLD) {
                SetCounter(-1);
            }
        }
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (counter >= 0) {
            SetCounter(0);
        }
    }
}