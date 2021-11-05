package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.beta.special.SheerCold;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SelfImmolationPower;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.*;

public class AyakaKamisato extends AnimatorCard {
    public static final EYBCardData DATA = Register(AyakaKamisato.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing).SetSeriesFromClassPackage()
            .SetMaxCopies(2)
            .PostInitialize(data -> data.AddPreview(new SheerCold(), false));
    public static final int THRESHOLD = 16;

    public AyakaKamisato() {
        super(DATA);

        Initialize(20, 0, 3, THRESHOLD);
        SetUpgrade(5, 0, 0, 0);
        SetAffinity_Blue(2, 0, 2);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Dark(0, 0, 3);

        SetEthereal(true);
        SetExhaust(true);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.CLAW)
                .forEach(d -> d.SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.TEAL, Color.LIGHT_GRAY, Color.SKY, Color.BLUE).duration * 0.1f));
        GameActions.Bottom.StackPower(new SelfImmolationPower(p, magicNumber));
        GameActions.Bottom.StackPower(new AyakaKamisatoPower(p, 1));
    }

    public static class AyakaKamisatoPower extends AnimatorPower
    {
        public AyakaKamisatoPower(AbstractCreature owner, int amount)
        {
            super(owner, AyakaKamisato.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            CheckCondition();
        }


        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card,m);
            if (card.block > 0) {
                ApplyVulnerable(amount);
                this.flash();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            RemovePower(GameActions.Delayed);
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(power, target, source);

            CheckCondition();
        }

        private void ApplyVulnerable(int amount) {
            if (GameUtilities.GetPowerAmount(owner, SelfImmolationPower.POWER_ID) > 0 && amount > 0) {
                GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(), 1);
            }
        }

        private void CheckCondition() {
            if (GameUtilities.GetPowerAmount(SelfImmolationPower.POWER_ID) > 0 && GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID) > THRESHOLD && CombatStats.TryActivateLimited(AyakaKamisato.DATA.ID))
            {
                AbstractCard c = new SheerCold();
                c.applyPowers();
                if (GameUtilities.IsPlayerTurn()) {
                    GameActions.Bottom.PlayCopy(c, null);
                }
                else {
                    c.use(player, null);
                }
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, CombatStats.CanActivateLimited(AyakaKamisato.DATA.ID) ? JUtils.Format(powerStrings.DESCRIPTIONS[1],THRESHOLD) : "");
        }
    }
}