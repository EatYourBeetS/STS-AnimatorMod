package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.vfx.ScreenHexagonEffect;
import eatyourbeets.powers.animator.IonizingStormPower;
import eatyourbeets.utilities.GameActions;

public class IonizingStorm extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IonizingStorm.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
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
        GameActions.Bottom.ChannelOrbs(Lightning::new, magicNumber);
        GameActions.Bottom.StackPower(new IonizingStormPower(p, 1));
        GameActions.Bottom.VFX(new ScreenHexagonEffect());
    }
}