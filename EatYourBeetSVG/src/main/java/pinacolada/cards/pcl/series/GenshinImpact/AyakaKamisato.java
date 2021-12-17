package pinacolada.cards.pcl.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.AdditiveSlashImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.SheerCold;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.powers.special.SelfImmolationPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class AyakaKamisato extends PCLCard {
    private static final Color FADE_EFFECT_COLOR = new Color(0.6f,0.6f,0.8f,0.5f);
    public static final PCLCardData DATA = Register(AyakaKamisato.class).SetAttack(2, CardRarity.RARE, PCLAttackType.Brutal).SetSeriesFromClassPackage()
            .SetMaxCopies(2)
            .PostInitialize(data -> data.AddPreview(new SheerCold(), false));
    public static final int THRESHOLD = 20;

    public AyakaKamisato() {
        super(DATA);

        Initialize(20, 0, 3, 8);
        SetUpgrade(5, 0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Dark(0, 0, 3);

        SetEthereal(true);
        SetExhaust(true);
        SetHitCount(2);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(THRESHOLD);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {


        PCLGameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
        SFX.Play(SFX.ATTACK_SCYTHE, 0.75f, 0.75f);
        SFX.Play(SFX.ATTACK_SCYTHE, 0.55f, 0.55f, 0.75f);
        PCLGameEffects.Queue.Add(new AdditiveSlashImpactEffect(m.hb.cX - 100f * Settings.scale, m.hb.cY + 100f * Settings.scale, Color.WHITE.cpy()));
        PCLGameEffects.Queue.Add(new AnimatedSlashEffect(m.hb.cX, m.hb.cY + 20f * Settings.scale,
                -500f, 0f, 260f, 8f, Color.LIGHT_GRAY.cpy(), Color.WHITE.cpy()));
        float wait = PCLGameEffects.Queue.Add(new AnimatedSlashEffect(m.hb.cX, m.hb.cY + 20f * Settings.scale,
                500f, 0f, 80f, 8f, Color.LIGHT_GRAY.cpy(), Color.WHITE.cpy())).duration * 6f;
        for (int i = 0; i < 4; i++) {
            PCLActions.Top.Wait(0.2f);
            PCLGameEffects.Queue.Add(new AnimatedSlashEffect(m.hb.cX - i * 10f * Settings.scale, m.hb.cY + 20f * Settings.scale,
                    500f, 0f, 80f, 8f, FADE_EFFECT_COLOR, FADE_EFFECT_COLOR));
        }
        PCLActions.Top.Wait(wait);

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SMASH)
                .forEach(d -> d.SetVFXColor(Color.RED.cpy(), Color.RED.cpy()).SetVFX(true, true).SetDamageEffect(c ->
                        {
                            SFX.Play(SFX.ATTACK_BUTCHER, 0.75f, 0.85f, 1.1f);
                            SFX.Play(SFX.ATTACK_WHIFF_2, 0.75f, 0.85f, 1.1f);
                            return PCLGameEffects.Queue.Add(VFX.Bleed(c.hb)).duration;
                        }

                ));

        PCLActions.Last.Callback(() -> {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
            {
                PCLActions.Bottom.LoseHP(secondaryValue, AttackEffects.NONE).IsCancellable(false);
            }
            else {
                PCLActions.Bottom.StackPower(new SelfImmolationPower(p, magicNumber));
                if (info.CanActivateLimited && !CheckCondition(0)) {
                    PCLActions.Bottom.StackPower(new AyakaKamisatoPower(p, 1));
                }
            }
        });
    }

    public static class AyakaKamisatoPower extends PCLPower
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
            CheckCondition(0);
        }


        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            RemovePower(PCLActions.Delayed);
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(power, target, source);

            CheckCondition(power.owner == player && DelayedDamagePower.POWER_ID.equals(power.ID) ? power.amount : 0);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, THRESHOLD);
        }
    }

    protected static boolean CheckCondition(int extraAmount) {
        if (PCLGameUtilities.GetPowerAmount(player, DelayedDamagePower.POWER_ID) + extraAmount >= THRESHOLD && CombatStats.TryActivateLimited(AyakaKamisato.DATA.ID))
        {
            AbstractCard c = new SheerCold();
            c.applyPowers();
            if (PCLGameUtilities.IsPlayerTurn()) {
                PCLActions.Bottom.PlayCopy(c, null);
            }
            else {
                c.use(player, null);
            }
            return true;
        }
        return false;
    }
}