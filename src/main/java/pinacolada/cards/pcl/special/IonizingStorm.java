package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.vfx.ScreenHexagonEffect;
import pinacolada.powers.special.IonizingStormPower;
import pinacolada.utilities.PCLActions;

public class IonizingStorm extends PCLCard
{
    public static final PCLCardData DATA = Register(IonizingStorm.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
    public static final int LIGHTNING_BONUS = 50;

    public IonizingStorm()
    {
        super(DATA);

        Initialize(0, 0, 2, IonizingStormPower.PER_CHARGE);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Light(1);
        SetAffinity_Silver(1);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(LIGHTNING_BONUS);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrbs(Lightning::new, magicNumber);
        PCLActions.Bottom.StackPower(new IonizingStormPower(p, 1));
        PCLActions.Bottom.VFX(new ScreenHexagonEffect());
    }
}