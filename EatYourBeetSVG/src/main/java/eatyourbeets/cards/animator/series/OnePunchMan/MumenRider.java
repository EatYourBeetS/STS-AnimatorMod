package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.utilities.GameActions;

public class MumenRider extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(MumenRider.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private int turns;

    public MumenRider()
    {
        super(DATA);

        Initialize(2, 0, 0, 0);
        SetUpgrade(1, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Light(1);

        SetCooldown(5, 1, this::OnCooldownCompleted, false, true);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SMASH);
        GameActions.Bottom.Cycle(name, 1);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.MoveCard(this, player.exhaustPile, player.hand)
                .ShowEffect(true, true);
    }
}