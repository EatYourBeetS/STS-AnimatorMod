package eatyourbeets.cards.animator.beta.series.Bleach;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.cards.animator.beta.status.Frostbite;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class ToushirouHitsugaya extends AnimatorCard implements Hidden {
    public static final EYBCardData DATA = Register(ToushirouHitsugaya.class).SetAttack(1, CardRarity.RARE, EYBAttackType.Normal).SetMaxCopies(2).SetSeriesFromClassPackage();

    static {
        DATA.AddPreview(new Frostbite(), true);
    }

    public ToushirouHitsugaya() {
        super(DATA);

        Initialize(6, 0, 5, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Orange(2, 0, 1);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade() {
        SetHaste(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo() {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

        for (int i = 0; i < magicNumber; i++) {
            int finalI = i + 1;
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).SetDamageEffect(
                    enemy -> {
                        SFX.Play(SFX.ATTACK_IRON_1, 1.35f + (0.2f * MathUtils.cosDeg(finalI * 90)));
                        return GameEffects.List.Add(
                                new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY,
                                        500 * MathUtils.cosDeg(finalI * 135),
                                        200 * MathUtils.sinDeg(finalI * 45),
                                        300 * MathUtils.cosDeg(finalI * 135),
                                        3, Color.BLUE.cpy(), Color.TEAL.cpy())).duration * 0.3f;
                    }
            );
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m) {
        return player.discardPile.size() > player.drawPile.size();
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        for (int i = 0; i < secondaryValue; i++) {
            GameActions.Bottom.MakeCardInDrawPile(new Frostbite())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }
}