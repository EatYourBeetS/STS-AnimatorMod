package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Urushihara extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Urushihara.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);

    private int lazyCounter;

    public Urushihara()
    {
        super(DATA);

        Initialize(23, 0);

        this.lazyCounter = 0;

        SetMultiDamage(true);
        SetEvokeOrbCount(1);
        SetSynergy(Synergies.HatarakuMaouSama);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        Urushihara other = (Urushihara) makeStatEquivalentCopy();

        other.lazyCounter = AbstractDungeon.cardRandomRng.random(3);

        if (!upgraded)
        {
            other.lazyCounter += 1;
        }

        GameActions.Bottom.ChannelOrb(new Dark(), true);

        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(other);
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

            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE)
            .SetPiercing(true, false);
            GameUtilities.UsePenNib();

            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}