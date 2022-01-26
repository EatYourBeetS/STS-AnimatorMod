package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.vfx.ScreenOnFireEffect3;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.special.BlazingHeatPower;
import pinacolada.utilities.PCLActions;

public class BlazingHeat extends PCLCard
{
    public static final PCLCardData DATA = Register(BlazingHeat.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
    public static final int BURNING_DAMAGE_BONUS = 100;
    public static final int FIRE_TRIGGER_BONUS = 100;
    public static final int FIRE_EVOKE_BONUS = 200;

    public BlazingHeat()
    {
        super(DATA);

        Initialize(0, 0, 2, FIRE_TRIGGER_BONUS);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Red(1);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(FIRE_EVOKE_BONUS);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Fire, magicNumber);
        PCLActions.Bottom.StackPower(new BlazingHeatPower(p, 1));
        PCLActions.Bottom.AddPowerEffectPassiveDamageBonus(BurningPower.POWER_ID, BURNING_DAMAGE_BONUS);
        PCLActions.Bottom.VFX(new ScreenOnFireEffect3());
    }
}