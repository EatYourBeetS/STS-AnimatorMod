package eatyourbeets.cards.animator.beta.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.LockOn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnDamageOverrideSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class AsukaLangley extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AsukaLangley.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Evangelion)
            .SetMultiformData(2, false);

    public AsukaLangley()
    {
        super(DATA);

        Initialize(13, 0, 1 );
        SetUpgrade(0, 0, 0 );

        SetAffinity_Orange(1, 0, 2);
        SetAffinity_Green(1);
        SetAffinity_Silver(1);

        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetHaste(false);
                SetAttackTarget(EYBCardTarget.ALL);
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
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (upgraded && auxiliaryData.form == 1) {
            GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.GUNSHOT).forEach(d -> d.SetVFXColor(Color.ORANGE).SetSoundPitch(0.5f, 0.6f));
        }
        else {
            GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetVFXColor(Color.ORANGE).SetSoundPitch(0.5f, 0.6f));;
        }
        GameActions.Bottom.StackPower(new AsukaLangleyPower(p));
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.LockOn, magicNumber * cards.size());
            }
        });
    }

    public static class AsukaLangleyPower extends AnimatorPower implements OnDamageOverrideSubscriber
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

            CombatStats.onDamageOverride.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onDamageOverride.Unsubscribe(this);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card,m);
            AnimatorCard aCard = JUtils.SafeCast(card, AnimatorCard.class);
            if (m != null && m.hasPower(LockOn.ID) && aCard != null && aCard.damage > 0 && aCard.damageTypeForTurn == DamageInfo.DamageType.NORMAL && aCard.attackType == EYBAttackType.Ranged)
            {
                GameActions.Last.ReducePower(m, this.owner, LockOn.ID, 1);
                this.flash();
            }
        }

        @Override
        public float OnDamageOverride(AbstractCreature target, DamageInfo.DamageType type, float damage, AbstractCard card)
        {
            AnimatorCard aCard = JUtils.SafeCast(card,AnimatorCard.class);
            if (aCard != null && aCard.attackType == EYBAttackType.Ranged && damage < aCard.baseDamage && target.hasPower(LockOn.ID)) {
                return aCard.baseDamage;
            }
            return damage;
        }

    }
}