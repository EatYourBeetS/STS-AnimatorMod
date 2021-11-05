package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.animator.IonizingStormPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class IonizingStorm extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IonizingStorm.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);

    public IonizingStorm()
    {
        super(DATA);

        Initialize(0, 0, 2, IonizingStormPower.PER_CHARGE);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Light(2);
        SetAffinity_Silver(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Lightning::new, magicNumber);
        GameActions.Bottom.StackPower(new IonizingStormPower(p, 1));
        GameEffects.Queue.Add(new BorderLongFlashEffect(Color.GOLDENROD, false));
        for (int i = 0; i < secondaryValue; i++) {
            GameEffects.Queue.Add(VFX.Lightning(p.hb));
        }

    }
}