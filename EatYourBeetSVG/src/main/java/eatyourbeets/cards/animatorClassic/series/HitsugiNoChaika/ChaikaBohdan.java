package eatyourbeets.cards.animatorClassic.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaBohdan extends AnimatorClassicCard implements OnAttackSubscriber
{
    public static final EYBCardData DATA = Register(ChaikaBohdan.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.COMMON);

    public ChaikaBohdan()
    {
        super(DATA);

        Initialize(6, 0, 3, 2);
        SetUpgrade(1, 0);

        
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
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
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        DamageModifiers.For(this).Set(0);
    }
}