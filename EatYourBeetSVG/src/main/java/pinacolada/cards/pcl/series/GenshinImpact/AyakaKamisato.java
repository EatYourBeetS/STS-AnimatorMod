package pinacolada.cards.pcl.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.AdditiveSlashImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.powers.special.SelfImmolationPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class AyakaKamisato extends PCLCard {
    private static final Color FADE_EFFECT_COLOR = new Color(0.6f,0.6f,0.8f,0.5f);
    public static final PCLCardData DATA = Register(AyakaKamisato.class)
            .SetAttack(3, CardRarity.RARE, PCLAttackType.Brutal)
            .SetSeriesFromClassPackage()
            .SetMaxCopies(2);

    public AyakaKamisato() {
        super(DATA);

        Initialize(32, 0, 4, 8);
        SetUpgrade(5, 0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Dark(1, 0, 4);

        SetEthereal(true);
        SetExhaust(true);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        boolean checkCache = CheckSpecialCondition(true);

        PreDamageAction(m);

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SMASH)
                .forEach(d -> d.SetVFXColor(Color.RED.cpy(), Color.RED.cpy())
                        .SetVFX(true, true)
                        .SetDamageEffect(c ->
                        {
                            SFX.Play(SFX.PCL_DECAPITATION, 0.55f, 0.65f, 1.2f);
                            SFX.Play(SFX.PCL_SPRAY, 1.1f, 1.25f, 1.3f);
                            return PCLGameEffects.Queue.Add(VFX.Bleed(c.hb)).duration * 0.4f;
                        })
                );

        PCLActions.Bottom.TakeDamage(secondaryValue, AttackEffects.CLAW).CanKill(false).IsCancellable(false);
        PCLActions.Bottom.StackPower(new SelfImmolationPower(player, magicNumber));
        PCLActions.Bottom.GainBlur(1);

        PCLActions.Last.Callback(() -> {
            if ((checkCache || CheckSpecialCondition(true)) && CombatStats.TryActivateLimited(cardID)) {
                final ArrayList<AbstractCard> choices = new ArrayList<>();
                choices.addAll(PCLJUtils.Filter(player.hand.group, c -> c.block > 0));
                choices.addAll(PCLJUtils.Filter(player.discardPile.group, c -> c.block > 0));
                choices.addAll(PCLJUtils.Filter(player.drawPile.group, c -> c.block > 0));

                AbstractCard maxBlockCard = PCLJUtils.FindMax(choices, c -> c.block);
                if (maxBlockCard != null) {
                    PCLActions.Bottom.PlayCard(maxBlockCard, m).AddCallback(() -> {
                        PCLActions.Last.Callback(() -> {
                            for (AbstractCreature cr: PCLGameUtilities.GetAllCharacters(true)) {
                                if (cr.powers != null) {
                                    for (AbstractPower po : cr.powers) {
                                        if (po instanceof DelayedDamagePower) {
                                            po.atEndOfTurn(PCLGameUtilities.IsPlayer(cr));
                                        }
                                    }
                                }
                            }
                        });
                    });
                }
            }
        });
    }

    public boolean CheckSpecialCondition(boolean tryUse){
        int damage = 0;
        for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents()) {
            damage += intent.GetDamage(true);
            if (damage >= player.currentHealth + player.currentBlock + PCLGameUtilities.GetTempHP()) {
                return true;
            }
        }
        return false;
    }

    protected WaitAction PreDamageAction(AbstractMonster m) {
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
        return PCLActions.Top.Wait(wait);
    }
}