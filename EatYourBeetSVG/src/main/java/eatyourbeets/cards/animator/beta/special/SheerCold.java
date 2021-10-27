package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.vfx.ScreenFreezingEffect;
import eatyourbeets.powers.animator.SheerColdPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;

public class SheerCold extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SheerCold.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
    public static final int FREEZING_REDUCTION_BONUS = 20;

    public SheerCold()
    {
        super(DATA);

        Initialize(0, 0, 2, FREEZING_REDUCTION_BONUS);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber);
        GameActions.Bottom.StackPower(new SheerColdPower(p, 1));
        GameActions.Bottom.Callback(() -> FreezingPower.AddPlayerReductionBonus(FREEZING_REDUCTION_BONUS));
        GameActions.Bottom.VFX(new ScreenFreezingEffect());
    }
}