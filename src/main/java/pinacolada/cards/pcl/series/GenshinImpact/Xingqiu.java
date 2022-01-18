package pinacolada.cards.pcl.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.orbs.TriggerOrbPassiveAbility;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Xingqiu extends PCLCard
{
    public static final PCLCardData DATA = Register(Xingqiu.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Piercing).SetSeriesFromClassPackage(true);

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
            return super.ModifyDamage(enemy, amount + PCLGameUtilities.GetPowerAmount(enemy, BurningPower.POWER_ID) * magicNumber);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
                .SetDamageEffect(enemy ->
                {
                    float wait = PCLGameEffects.List.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 30f * Settings.scale,
                            500f, 200f, 290f, 3f, Color.TEAL.cpy(), Color.BLUE.cpy())).duration;
                    wait += PCLGameEffects.Queue.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 60f * Settings.scale,
                            500f, 200f, 290f, 5f, Color.TEAL.cpy(), Color.BLUE.cpy())).duration;
                    SFX.Play(SFX.ATTACK_REAPER);
                    return wait * 0.55f;
                }));
        if (m.hasPower(BurningPower.POWER_ID)) {
            PCLActions.Bottom.RemovePower(p, m, BurningPower.POWER_ID);
        }
        else {
            PCLActions.Bottom.ApplyRippled(TargetHelper.Normal(m), magicNumber);
        }

        PCLActions.Bottom.StackPower(new XingqiuPower(p, secondaryValue));
    }

    public static class XingqiuPower extends PCLPower
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
                PCLActions.Bottom.Callback(new TriggerOrbPassiveAbility(amount));
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

