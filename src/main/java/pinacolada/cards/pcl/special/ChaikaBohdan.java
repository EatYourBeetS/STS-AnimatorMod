package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.modifiers.DamageModifiers;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ChaikaBohdan extends PCLCard implements OnAttackSubscriber
{
    public static final PCLCardData DATA = Register(ChaikaBohdan.class)
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
        PCLActions.Bottom.GainVelocity(secondaryValue);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player.hand.contains(this))
        {
            PCLCombatStats.onAttack.Subscribe(this);
        }
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (player.hand.contains(this))
        {
            if (info.type == DamageInfo.DamageType.NORMAL && PCLGameUtilities.IsMonster(target))
            {
                DamageModifiers.For(this).Add(secondaryValue);
                this.flash();
            }
        }
        else
        {
            PCLCombatStats.onAttack.Unsubscribe(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        DamageModifiers.For(this).Set(0);
    }
}