package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.utilities.PCLActions;

public class Bozes extends PCLCard
{
    public static final PCLCardData DATA = Register(Bozes.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage();

    public Bozes()
    {
        super(DATA);

        Initialize(6, 0, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        if (IsStarter()) {
            PCLActions.Bottom.Motivate(magicNumber);
        }
        PCLActions.Bottom.StackPower(new BozesPower(p, this.secondaryValue));
    }

    public static class BozesPower extends PCLPower implements OnSynergySubscriber
    {
        public BozesPower(AbstractCreature owner, int amount)
        {
            super(owner, Bozes.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void OnSynergy(AbstractCard card)
        {
            PCLActions.Bottom.StackPower(new SupportDamagePower(owner, amount));
            this.flash();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}