package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.AdditiveSlashImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.cards.animator.beta.special.SheerCold;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SelfImmolationPower;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class AyakaKamisato extends AnimatorCard {
    private static final Color FADE_EFFECT_COLOR = new Color(0.6f,0.6f,0.8f,0.5f);
    public static final EYBCardData DATA = Register(AyakaKamisato.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Brutal).SetSeriesFromClassPackage()
            .SetMaxCopies(2)
            .PostInitialize(data -> data.AddPreview(new SheerCold(), false));
    public static final int THRESHOLD = 4;
    public static final int PERCENTAGE = 50;

    public AyakaKamisato() {
        super(DATA);

        Initialize(20, 0, 3, 4);
        SetUpgrade(5, 0, 0, 0);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Dark(0, 0, 2);

        SetEthereal(true);
        SetExhaust(true);
        SetHitCount(2);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(PERCENTAGE, THRESHOLD);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {


        GameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
        SFX.Play(SFX.ATTACK_SCYTHE, 0.75f, 0.75f);
        GameEffects.Queue.Add(new AdditiveSlashImpactEffect(m.hb.cX - 50f * Settings.scale, m.hb.cY + 100f * Settings.scale, Color.WHITE.cpy()));
        float wait = GameEffects.Queue.Add(new AnimatedSlashEffect(m.hb.cX, m.hb.cY + 20f * Settings.scale,
                500f, 0f, 80f, 8f, Color.LIGHT_GRAY.cpy(), Color.WHITE.cpy())).duration * 6f;
        for (int i = 0; i < 4; i++) {
            GameActions.Top.Wait(0.2f);
            GameEffects.Queue.Add(new AnimatedSlashEffect(m.hb.cX - i * 10f * Settings.scale, m.hb.cY + 20f * Settings.scale,
                    500f, 0f, 80f, 8f, FADE_EFFECT_COLOR, FADE_EFFECT_COLOR));
        }
        GameActions.Top.Wait(wait);


        GameActions.Bottom.SFX(SFX.ATTACK_SCIMITAR, 0.75f, 0.8f);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SMASH)
                .forEach(d -> d.SetVFXColor(Color.RED.cpy(), Color.RED.cpy()).SetVFX(true, true).SetDamageEffect(c ->
                        GameEffects.Queue.Add(VFX.Bleed(c.hb)).duration));

        GameActions.Bottom.TakeDamage(secondaryValue, AttackEffects.NONE).IsCancellable(false);
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
                if (GameUtilities.GetPowerAmount(owner, SelfImmolationPower.POWER_ID) > 0 && amount > 0) {
                    for (AbstractCreature cr : GameUtilities.GetAllCharacters(true)) {
                        GameActions.Bottom.DealDamageAtEndOfTurn(owner, cr, card.block, AttackEffects.CLAW);
                    }
                }
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

        private void CheckCondition() {
            if (GameUtilities.GetPowerAmount(SelfImmolationPower.POWER_ID) > 0 && GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID) >= THRESHOLD && CombatStats.TryActivateLimited(AyakaKamisato.DATA.ID))
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
            description = FormatDescription(0, PERCENTAGE, CombatStats.CanActivateLimited(AyakaKamisato.DATA.ID) ? JUtils.Format(powerStrings.DESCRIPTIONS[1],THRESHOLD) : "");
        }
    }
}