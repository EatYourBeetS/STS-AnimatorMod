package eatyourbeets.cards.animator.beta.series.Bleach;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.cards.animator.beta.special.SheerCold;
import eatyourbeets.cards.animator.beta.status.Status_Frostbite;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

import java.util.concurrent.atomic.AtomicInteger;

public class ToushirouHitsugaya_Bankai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ToushirouHitsugaya_Bankai.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new Status_Frostbite(), false);
                data.AddPreview(new SheerCold(), false);
            });

    public ToushirouHitsugaya_Bankai() {
        super(DATA);

        Initialize(3, 0, 4, 3);
        SetUpgrade(0, 0, 0);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Blue(2, 0, 2);

        SetExhaust(true);
        SetHitCount(5);
    }

    @Override
    protected void OnUpgrade() {
        SetHaste(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

        AtomicInteger i = new AtomicInteger();
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE).forEach(d -> {d.SetDamageEffect(
                (enemy, __) -> {
                    SFX.Play(SFX.ATTACK_IRON_1, 1.75f + (0.2f * MathUtils.cosDeg(i.get() * 90)));
                    GameEffects.List.Add(
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
        GameActions.Bottom.ExhaustFromHand(name, magicNumber, true).SetFilter(c -> c.type == CardType.STATUS).AddCallback(cards -> {
            if (cards.size() > secondaryValue && info.TryActivateLimited()) {
                AbstractCard c = new SheerCold();
                c.applyPowers();
                c.use(player, null);
            }
        });
        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.MakeCardInDrawPile(new Status_Frostbite())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }
}