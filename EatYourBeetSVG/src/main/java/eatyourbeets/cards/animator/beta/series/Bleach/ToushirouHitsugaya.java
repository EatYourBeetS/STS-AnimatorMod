package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.status.Frostbite;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class ToushirouHitsugaya extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(ToushirouHitsugaya.class).SetAttack(1, CardRarity.RARE, EYBAttackType.Normal).SetMaxCopies(2).SetSeriesFromClassPackage();

    static {
        DATA.AddPreview(new Frostbite(), true);
    }

    public ToushirouHitsugaya() {
        super(DATA);

        Initialize(6, 0, 3, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Orange(2, 0, 1);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), magicNumber).ShowEffect(true, true);
    }
}