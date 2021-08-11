package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.LockOn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class AsukaLangley extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AsukaLangley.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Evangelion);

    public AsukaLangley()
    {
        super(DATA);

        Initialize(8, 0, 1 );
        SetUpgrade(1, 0, 0 );

        SetAffinity_Orange(1, 0, 2);
        SetAffinity_Green(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + Math.floorDiv(player.gold, secondaryValue));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);
        GameActions.Bottom.StackPower(new AsukaLangleyPower(p));
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.LockOn, magicNumber * cards.size());
            }
        });
    }

    public static class AsukaLangleyPower extends AnimatorPower
    {
        public AsukaLangleyPower(AbstractPlayer owner)
        {
            super(owner, AsukaLangley.DATA);

            Initialize(-1);
        }

        @Override
        public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster enemy)
        {
            AnimatorCard aCard = JUtils.SafeCast(card,AnimatorCard.class);
            if (aCard != null && aCard.attackType == EYBAttackType.Ranged && damage < aCard.baseDamage && enemy.hasPower(LockOn.ID)) {
                return super.atDamageFinalGive(aCard.baseDamage, type, card, enemy);
            }
            return super.atDamageFinalGive(damage, type, card, enemy);
        }

        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
        {
            super.onAttack(info, damageAmount, target);
            AbstractCard lastCardPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1);
            AnimatorCard aCard = JUtils.SafeCast(lastCardPlayed, AnimatorCard.class);

            if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL && target.hasPower(LockOn.ID) && aCard != null && aCard.attackType == EYBAttackType.Ranged)
            {
                GameActions.Bottom.ReducePower(target, this.owner, LockOn.ID, 1);
                this.flash();
            }
        }

    }
}