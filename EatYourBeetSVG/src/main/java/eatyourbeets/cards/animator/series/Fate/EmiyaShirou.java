package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class EmiyaShirou extends AnimatorCard implements OnAttackSubscriber
{
    public static final EYBCardData DATA = Register(EmiyaShirou.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();
    private Fire fireOrb;

    public EmiyaShirou()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(3, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Light(1, 1, 0);
        SetAffinity_Blue(0,0,1);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.Add(new RandomCardUpgrade());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE);

        fireOrb = JUtils.SafeCast(GameUtilities.GetFirstOrb(Fire.ORB_ID),Fire.class);
        if (fireOrb != null) {
            fireOrb.IncreaseBasePassiveAmount(magicNumber);
            fireOrb.IncreaseBaseEvokeAmount(magicNumber);
        }
        else {
            GameActions.Bottom.ChannelOrb(new Fire());
        }

        CombatStats.onAttack.Subscribe(this);

        if (info.IsSynergizing) {
            GameActions.Bottom.Add(new RandomCardUpgrade());
        }

    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        for (AbstractOrb o: player.orbs) {
            if (o == fireOrb) {
                GameActions.Bottom.EvokeOrb(1, fireOrb);
            }
        }
        CombatStats.onAttack.Unsubscribe(this);
    }
}