package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Lean extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lean.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Lean()
    {
        super(DATA);

        Initialize(0, 0, 2, 15);
        SetUpgrade(0,0,1);

        SetAffinity_Fire();
        SetAffinity_Air();
        SetAffinity_Thunder();

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int supportDamageAmount = 0;

        for (AbstractCard c : p.hand.group)
        {
            if (GameUtilities.HasAffinity(c, Affinity.Fire) || GameUtilities.HasAffinity(c, Affinity.Air) || GameUtilities.HasAffinity(c, Affinity.Thunder))
            {
                supportDamageAmount += magicNumber;
            }
        }

        GameActions.Bottom.StackPower(new SupportDamagePower(p, supportDamageAmount))
        .AddCallback(power ->
        {
            SupportDamagePower supportDamage = JUtils.SafeCast(power, SupportDamagePower.class);
            if (supportDamage != null && supportDamage.amount > secondaryValue)
            {
                GameActions.Bottom.ChannelRandomCommonOrb(rng);
            }
        });
    }
}