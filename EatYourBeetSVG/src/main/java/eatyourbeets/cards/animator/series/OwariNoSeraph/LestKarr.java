package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Overlord.CZDelta;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.TargetHelper;

public class LestKarr extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(LestKarr.class)
            .SetAttack(0, CardRarity.COMMON, EYBAttackType.Piercing, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public LestKarr()
    {
        super(DATA);

        Initialize(11, 0, 2);
        SetUpgrade(0,0,-1);
        SetAffinity_Thunder();

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, GR.Enums.AttackEffect.SPEAR);

        CombatStats.onStartOfTurnPostDraw.Subscribe((CZDelta) makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw() {
        GameActions.Bottom.Callback(() ->
        {
            GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
            GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.TemporaryDexterity, -magicNumber);
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        });
    }
}