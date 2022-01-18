package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.LockOn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnDamageOverrideSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class AsukaLangley extends PCLCard
{
    public static final PCLCardData DATA = Register(AsukaLangley.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Evangelion)
            .SetMultiformData(2, false);

    public AsukaLangley()
    {
        super(DATA);

        Initialize(13, 0, 1 );
        SetUpgrade(0, 0, 0 );

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(1);
        SetAffinity_Silver(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetHaste(false);
                SetAttackTarget(eatyourbeets.cards.base.EYBCardTarget.ALL);
                SetMultiDamage(true);
                upgradedDamage = true;
            }
            else {
                Initialize(15, 0, 1 );
                SetUpgrade(0, 0, 0 );
                SetHaste(true);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (upgraded && auxiliaryData.form == 1) {
            PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.GUNSHOT).forEach(d -> d.SetVFXColor(Color.ORANGE).SetSoundPitch(0.5f, 0.6f));
        }
        else {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetVFXColor(Color.ORANGE).SetSoundPitch(0.5f, 0.6f));
        }
        PCLActions.Bottom.StackPower(new AsukaLangleyPower(p));
        PCLActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PCLPowerHelper.LockOn, magicNumber * cards.size());
            }
        });
    }

    public static class AsukaLangleyPower extends PCLPower implements OnDamageOverrideSubscriber
    {
        public AsukaLangleyPower(AbstractPlayer owner)
        {
            super(owner, AsukaLangley.DATA);

            Initialize(-1);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onDamageOverride.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onDamageOverride.Unsubscribe(this);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card,m);
            PCLCard aCard = PCLJUtils.SafeCast(card, PCLCard.class);
            if (m != null && m.hasPower(LockOn.ID) && aCard != null && aCard.damage > 0 && aCard.damageTypeForTurn == DamageInfo.DamageType.NORMAL && aCard.attackType == PCLAttackType.Ranged)
            {
                PCLActions.Last.ReducePower(m, this.owner, LockOn.ID, 1);
                this.flash();
            }
        }

        @Override
        public float OnDamageOverride(AbstractCreature target, DamageInfo.DamageType type, float damage, AbstractCard card)
        {
            PCLCard aCard = PCLJUtils.SafeCast(card, PCLCard.class);
            if (aCard != null && aCard.attackType == PCLAttackType.Ranged && damage < aCard.baseDamage && target.hasPower(LockOn.ID)) {
                return aCard.baseDamage;
            }
            return damage;
        }

    }
}