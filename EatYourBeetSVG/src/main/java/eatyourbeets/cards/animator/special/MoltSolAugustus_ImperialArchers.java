package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.GATE.MoltSolAugustus;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MoltSolAugustus_ImperialArchers extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MoltSolAugustus_ImperialArchers.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(MoltSolAugustus.DATA.Series);

    public MoltSolAugustus_ImperialArchers()
    {
        super(DATA);

        Initialize(0, 0, 50);
        SetUpgrade(0, 0, 100);

        SetAffinity_Earth();

        SetEthereal(true);
        SetExhaust(true);
    }
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int supportDamageAmount = GameUtilities.GetPowerAmount(SupportDamagePower.POWER_ID);

        if (supportDamageAmount == 0)
        {
            return;
        }

        int supportDamageToGain = supportDamageAmount + (int)Math.floor((supportDamageAmount * magicNumber) / 100f);

        if (supportDamageToGain >= 0) {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, supportDamageToGain));
        }
    }
}