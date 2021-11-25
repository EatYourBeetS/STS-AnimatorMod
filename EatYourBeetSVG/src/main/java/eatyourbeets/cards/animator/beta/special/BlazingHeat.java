package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.vfx.megacritCopy.ScreenOnFireEffect2;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.animator.BlazingHeatPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;

public class BlazingHeat extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BlazingHeat.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
    public static final int BURNING_DAMAGE_BONUS = 100;
    public static final int FIRE_TRIGGER_BONUS = 100;
    public static final int FIRE_EVOKE_BONUS = 200;

    public BlazingHeat()
    {
        super(DATA);

        Initialize(0, 0, 2, FIRE_TRIGGER_BONUS);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Red(2);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(FIRE_EVOKE_BONUS);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Fire::new, magicNumber);
        GameActions.Bottom.StackPower(new BlazingHeatPower(p, 1));
        GameActions.Bottom.Callback(() -> CommonTriggerablePower.AddPlayerDamageBonus(BurningPower.POWER_ID, BURNING_DAMAGE_BONUS));
        GameActions.Bottom.VFX(new ScreenOnFireEffect2());
    }
}