package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TupleT3;

public class YaoHaDucy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YaoHaDucy.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private TupleT3<AbstractCard, Boolean, Integer> synergyCheckCache = new TupleT3<>(null, false, 0);

    public YaoHaDucy()
    {
        super(DATA);

        Initialize(2, 0, 1, 2);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Green(1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && !GameUtilities.HasArtifact(m))
        {
            GameUtilities.GetIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        GameActions.Bottom.ReduceStrength(m, magicNumber, true);

        if (m.hasPower(PoisonPower.POWER_ID))
        {
            GameActions.Bottom.TryChooseGainAffinity(name, secondaryValue, Affinity.Green, Affinity.Orange);
        }
    }
}