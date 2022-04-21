package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TupleT3;

public class YaoHaDucy extends AnimatorCard implements OnAffinitySealedSubscriber
{
    public static final EYBCardData DATA = Register(YaoHaDucy.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private TupleT3<AbstractCard, Boolean, Integer> synergyCheckCache = new TupleT3<>(null, false, 0);

    public YaoHaDucy()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Green(1);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        CombatStats.onAffinitySealed.Subscribe(this);
    }

    @Override
    public void OnAffinitySealed(EYBCard card, boolean manual)
    {
        if (card == this) {
            this.affinities.sealed = false;
        }
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
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL);

        if (!GameUtilities.HasArtifact(m))
        {
            GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        }
    }
}