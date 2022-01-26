package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.vfx.ScreenFreezingEffect;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.FreezingPower;
import pinacolada.powers.special.SheerColdPower;
import pinacolada.utilities.PCLActions;

public class SheerCold extends PCLCard
{
    public static final PCLCardData DATA = Register(SheerCold.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
    public static final int FREEZING_REDUCTION_BONUS = 20;
    public static final int FROST_TRIGGER_BONUS = 50;
    public static final int FROST_EVOKE_BONUS = 100;

    public SheerCold()
    {
        super(DATA);

        Initialize(0, 0, 2, FREEZING_REDUCTION_BONUS);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(1);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(FROST_TRIGGER_BONUS, FROST_EVOKE_BONUS);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Frost, magicNumber);
        PCLActions.Bottom.StackPower(new SheerColdPower(p, 1));
        PCLActions.Bottom.Callback(() -> PCLCombatStats.AddEffectBonus(FreezingPower.POWER_ID, FREEZING_REDUCTION_BONUS));
        PCLActions.Bottom.VFX(new ScreenFreezingEffect());
    }
}