package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import pinacolada.actions.special.CreateRandomCurses;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SoulGem1 extends PCLRelic
{
    public static final String ID = CreateFullID(SoulGem1.class);
    public static final int POWER_AMOUNT = 3;
    public static final int TRIGGER_THRESHOLD = 6;

    public SoulGem1()
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
        if (c.type == AbstractCard.CardType.ATTACK) {
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
        int oldAmount = PCLGameUtilities.GetPowerAmount(player, StrengthPower.POWER_ID);
        if (oldAmount < POWER_AMOUNT) {
            PCLActions.Bottom.GainStrength(POWER_AMOUNT - oldAmount);
        }
    }
}