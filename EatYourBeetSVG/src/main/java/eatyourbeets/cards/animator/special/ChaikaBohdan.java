package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaBohdan extends AnimatorCard implements OnAttackSubscriber
{
    public static final EYBCardData DATA = Register(ChaikaBohdan.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika);

    public ChaikaBohdan()
    {
        super(DATA);

        Initialize(6, 0, 3, 2);
        SetUpgrade(1, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Light(0,0,1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Bottom.GainVelocity(secondaryValue);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player.hand.contains(this))
        {
            CombatStats.onAttack.Subscribe(this);
        }
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (player.hand.contains(this))
        {
            if (info.type == DamageInfo.DamageType.NORMAL && GameUtilities.IsMonster(target))
            {
                DamageModifiers.For(this).Add(secondaryValue);
                this.flash();
            }
        }
        else
        {
            CombatStats.onAttack.Unsubscribe(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        DamageModifiers.For(this).Set(0);
    }
}