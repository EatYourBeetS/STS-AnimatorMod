package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.NagisaMomoe_Charlotte;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NagisaMomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NagisaMomoe.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false)
            .PostInitialize(data ->
            {
                data.AddPreview(new NagisaMomoe_Charlotte(), true);
            });

    public NagisaMomoe()
    {
        super(DATA);

        Initialize(0, 0, 5, 2);
        SetUpgrade(0, 0, 2);

        SetAffinity_Star(1);

        SetHealing(true);
        SetEthereal(true);
        SetExhaust(true);
        SetSoul(1, 0, NagisaMomoe_Charlotte::new);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            Initialize(0, 0, 1, 3);
            SetUpgrade(0, 0, 0, 0);
        }
        else {
            Initialize(0, 0, 2, 2);
            SetUpgrade(0, 0, 0, 0);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainWisdom(magicNumber);
        GameActions.Bottom.GainInvocation(magicNumber);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this) && GameUtilities.HasLightAffinity(c))
        {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
            GameActions.Bottom.Flash(this);
        }
    }
}