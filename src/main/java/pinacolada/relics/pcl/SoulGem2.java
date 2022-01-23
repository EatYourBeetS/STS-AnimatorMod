package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import pinacolada.actions.special.CreateRandomCurses;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SoulGem2 extends PCLRelic
{
    public static final String ID = CreateFullID(SoulGem2.class);
    public static final int POWER_AMOUNT = 3;
    public static final int TRIGGER_THRESHOLD = 5;

    public SoulGem2()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        SetCounter(0);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c.baseBlock > 0) {
            AddCounter(1);
            if (counter > TRIGGER_THRESHOLD) {
                PCLActions.Bottom.Add(new CreateRandomCurses(1, player.drawPile));
                SetCounter(0);
            }
            flash();
        }
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();
        int oldAmount = PCLGameUtilities.GetPowerAmount(player, DexterityPower.POWER_ID);
        if (oldAmount < POWER_AMOUNT) {
            PCLActions.Bottom.GainDexterity(POWER_AMOUNT - oldAmount);
        }
    }
}