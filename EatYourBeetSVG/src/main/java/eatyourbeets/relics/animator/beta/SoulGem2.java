package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SoulGem2 extends AnimatorRelic implements OnApplyPowerSubscriber
{
    public static final String ID = CreateFullID(SoulGem2.class);
    public static final int POWER_AMOUNT = 3;
    public static final int TRIGGER_THRESHOLD = 5;

    public SoulGem2()
    {
        super(ID, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        GameActions.Bottom.GainDexterity(POWER_AMOUNT);
        CombatStats.onApplyPower.Subscribe(this);
        SetCounter(0);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c.baseBlock > 0) {
            AddCounter(1);
            if (counter > TRIGGER_THRESHOLD) {
                GameActions.Bottom.Add(new CreateRandomCurses(1, player.drawPile));
                SetCounter(0);
            }
            flash();
        }
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        int oldAmount = GameUtilities.GetPowerAmount(target, DexterityPower.POWER_ID);
        if (GameUtilities.IsPlayer(target) && oldAmount < POWER_AMOUNT) {
            GameUtilities.ApplyPowerInstantly(player, PowerHelper.Dexterity, -oldAmount);
            GameActions.Bottom.GainDexterity(POWER_AMOUNT);
        }
    }
}