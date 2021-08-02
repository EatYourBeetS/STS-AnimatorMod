package eatyourbeets.cards.animator.colorless.uncommon;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Urushihara extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Urushihara.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HatarakuMaouSama);

    private int lazyCounter;

    public Urushihara()
    {
        super(DATA);

        Initialize(23, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        this.lazyCounter = 0;
        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        Urushihara other = (Urushihara) makeStatEquivalentCopy();

        other.lazyCounter = rng.random(3);

        if (!upgraded)
        {
            other.lazyCounter += 1;
        }

        GameActions.Bottom.ChannelOrb(new Dark());

        CombatStats.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (lazyCounter > 0)
        {
            lazyCounter -= 1;
        }
        else
        {
            applyPowers();

            GameEffects.Queue.ShowCardBriefly(this);

            GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
            GameUtilities.UsePenNib();

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}