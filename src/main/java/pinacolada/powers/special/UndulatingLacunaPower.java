package pinacolada.powers.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.RippledPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import static pinacolada.cards.pcl.special.UndulatingLacuna.DARK_BONUS;

public class UndulatingLacunaPower extends PCLPower implements OnOrbPassiveEffectSubscriber
{
    public static final int GAIN_PERCENT = 3;
    public static final String POWER_ID = CreateFullID(UndulatingLacunaPower.class);

    public UndulatingLacunaPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onOrbPassiveEffect.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, DARK_BONUS * amount, GAIN_PERCENT);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Dark.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.passiveAmount * DARK_BONUS * amount / 100);
        }
    }

    @Override
    public void OnOrbPassiveEffect(AbstractOrb orb) {
        if (Dark.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.passiveAmount * DARK_BONUS * amount / 100);
        }
    }

    @Override
    public void onCardDraw(AbstractCard card)
    {
        super.onCardDraw(card);

        if (!PCLGameUtilities.IsPlayable(card))
        {
            PCLActions.Bottom.Callback(() -> PCLCombatStats.AddEffectBonus(RippledPower.POWER_ID, GAIN_PERCENT));
        }
    }

    private void makeMove(AbstractOrb orb, int applyAmount) {
        PCLActions.Bottom.VFX(VFX.CircularWave(orb.hb).SetColors(Color.BLACK, Color.VIOLET));
        PCLActions.Bottom.VFX(VFX.ShockWave(orb.hb, Color.PURPLE), 0.3f);
        PCLActions.Bottom.ApplyRippled(TargetHelper.Enemies(), applyAmount).CanStack(true);
    }

    private void applyPower(AbstractCreature target, AbstractOrb orb, int applyAmount) {
    }
}
