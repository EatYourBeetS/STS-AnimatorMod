package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.EYBClickablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class MikuIzayoi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MikuIzayoi.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public MikuIzayoi()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetAffinity_Light(1, 1, 0);
        SetEthereal(true);
        SetProtagonist(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.GainInspiration(secondaryValue);
        if (CheckSpecialCondition(true)) {
            GameActions.Bottom.GainInspiration(secondaryValue);
        }

        if (info.IsSynergizing && GameUtilities.IsSameSeries(this,info.PreviousCard)) {
            GameActions.Bottom.Motivate(secondaryValue);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return JUtils.Count(player.powers, po -> po instanceof EYBClickablePower) >= magicNumber;
    }
}