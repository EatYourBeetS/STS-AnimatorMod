package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Ranga extends AnimatorCard implements OnEvokeOrbSubscriber
{
    public static final EYBCardData DATA = Register(Ranga.class)
            .SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Ranga()
    {
        super(DATA);

        Initialize(6, 0);

        SetAffinity_Green(1);
        SetAffinity_Light(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAttackTarget(EYBCardTarget.ALL);
        SetMultiDamage(true);
        upgradedDamage = true;
    }

    @Override
    public void OnEvokeOrb(AbstractOrb orb)
    {
        if ((orb.ID.equals(Lightning.ORB_ID) || orb.ID.equals(Dark.ORB_ID)) && player.exhaustPile.contains(this))
        {
            GameActions.Last.MoveCard(this, player.exhaustPile, player.hand)
            .AddCallback(c -> ((Ranga)c).SetPurge(true, true));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (upgraded)
        {
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.LIGHTNING);
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.LIGHTNING);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onEvokeOrb.Subscribe(this);
    }
}