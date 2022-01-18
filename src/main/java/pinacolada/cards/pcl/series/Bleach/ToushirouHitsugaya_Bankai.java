package pinacolada.cards.pcl.series.Bleach;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.SheerCold;
import pinacolada.cards.pcl.status.Status_Frostbite;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

import java.util.concurrent.atomic.AtomicInteger;

public class ToushirouHitsugaya_Bankai extends PCLCard
{
    public static final PCLCardData DATA = Register(ToushirouHitsugaya_Bankai.class).SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Ice, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new Status_Frostbite(), false);
                data.AddPreview(new SheerCold(), false);
            });

    public ToushirouHitsugaya_Bankai() {
        super(DATA);

        Initialize(4, 0, 4, 3);
        SetUpgrade(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(1, 0, 2);

        SetExhaust(true);
        SetHitCount(5);
    }

    @Override
    protected void OnUpgrade() {
        SetHaste(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

        AtomicInteger i = new AtomicInteger();
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE).forEach(d -> {d.SetDamageEffect(
                (enemy, __) -> {
                    SFX.Play(SFX.ATTACK_IRON_1, 1.75f + (0.2f * MathUtils.cosDeg(i.get() * 90)));
                    PCLGameEffects.List.Add(
                            new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY,
                                    500 * MathUtils.cosDeg(i.get() * 135),
                                    500 * MathUtils.sinDeg(i.get() * 45),
                                    300 * MathUtils.cosDeg(i.get() * 135),
                                    3, Color.BLUE.cpy(), Color.TEAL.cpy()));
                }
        );
        i.addAndGet(1);});
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.ExhaustFromHand(name, magicNumber, true)
                .SetOptions(true, true, true)
                .SetFilter(c -> c.type == CardType.STATUS).AddCallback(cards -> {
            if (cards.size() > secondaryValue && info.TryActivateLimited()) {
                AbstractCard c = new SheerCold();
                c.applyPowers();
                c.use(player, null);
            }
        });
        for (int i = 0; i < magicNumber; i++) {
            PCLActions.Bottom.MakeCardInDrawPile(new Status_Frostbite())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }
}