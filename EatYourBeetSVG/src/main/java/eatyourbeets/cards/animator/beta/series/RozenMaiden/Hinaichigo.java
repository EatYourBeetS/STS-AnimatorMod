package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.stances.SuperchargeStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Hinaichigo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hinaichigo.class)
    		.SetSkill(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public Hinaichigo()
    {
        super(DATA);

        Initialize(0, 0, 2, 6);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Light(1, 1, 0);

        SetAffinityRequirement(Affinity.Blue, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        if (GameUtilities.GetPowerAmount(m, PoisonPower.POWER_ID) > 0) {
            GameActions.Bottom.ApplyWeak(p, m, magicNumber);
        }
        else {
            GameActions.Bottom.ApplyPoison(p, m, secondaryValue);
        }

        if (IsStarter() && TrySpendAffinity(Affinity.Blue)) {
            GameActions.Bottom.ChangeStance(SuperchargeStance.STANCE_ID);
        }
    }
}
