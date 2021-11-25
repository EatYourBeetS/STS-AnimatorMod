package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Xingqiu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Xingqiu.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Piercing).SetSeriesFromClassPackage(true);

    public Xingqiu()
    {
        super(DATA);

        Initialize(2, 0, 4, 1);
        SetAffinity_Blue(1, 0, 4);
        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetExhaust(false);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            return super.ModifyDamage(enemy, amount + GameUtilities.GetPowerAmount(enemy, BurningPower.POWER_ID) * magicNumber);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).forEach(d -> d
                .SetDamageEffect(enemy ->
                {
                    float wait = GameEffects.List.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 30f * Settings.scale,
                            500f, 200f, 290f, 3f, Color.TEAL.cpy(), Color.BLUE.cpy())).duration;
                    wait += GameEffects.Queue.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 60f * Settings.scale,
                            500f, 200f, 290f, 5f, Color.TEAL.cpy(), Color.BLUE.cpy())).duration;
                    SFX.Play(SFX.ATTACK_REAPER);
                    return wait * 0.55f;
                }));
        GameActions.Bottom.RemovePower(p, m, BurningPower.POWER_ID);
        GameActions.Bottom.StackPower(new XingqiuPower(p, secondaryValue));
    }

    public static class XingqiuPower extends AnimatorPower
    {
        public XingqiuPower(AbstractCreature owner, int amount)
        {
            super(owner, Xingqiu.DATA);

            Initialize(amount);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (card.type == CardType.ATTACK && card.costForTurn >= 1)
            {
                GameActions.Bottom.Callback(new TriggerOrbPassiveAbility(amount));
                this.flashWithoutSound();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}

