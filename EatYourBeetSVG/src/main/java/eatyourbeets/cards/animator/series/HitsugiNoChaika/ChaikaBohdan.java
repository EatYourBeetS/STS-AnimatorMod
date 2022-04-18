package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaBohdan extends AnimatorCard implements OnAttackSubscriber
{
    public static final EYBCardData DATA = Register(ChaikaBohdan.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public ChaikaBohdan()
    {
        super(DATA);

        Initialize(4, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(2);

        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.Callback(() -> DamageModifiers.For(this).Set(0));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Cycle(name, 1);
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (player.hand.contains(this) && info.type == DamageInfo.DamageType.NORMAL && GameUtilities.IsMonster(target))
        {
            DamageModifiers.For(this).Add(magicNumber);
            flash();
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAttack.Subscribe(this);
    }
}