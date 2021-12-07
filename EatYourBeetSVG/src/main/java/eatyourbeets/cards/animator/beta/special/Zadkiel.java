package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Zadkiel extends AnimatorCard {
    public static final EYBCardData DATA = Register(Zadkiel.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Brutal, EYBCardTarget.Random).SetSeries(CardSeries.DateALive);

    public Zadkiel() {
        super(DATA);

        Initialize(36, 0, 2, 9);
        SetUpgrade(10, 0, 0);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Blue(0, 0, 2);
        SetAffinity_Dark(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
        GameActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.SMASH).forEach(d -> d
                .SetVFXColor(Color.NAVY)
                .SetDamageEffect(e -> GameEffects.List.Add(VFX.Bite(e.hb, Color.NAVY)).duration)
                .AddCallback(enemy -> {
                    GameActions.Top.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);
                    if (GameUtilities.IsFatal(enemy, true)) {
                        GameActions.Bottom.EvokeOrb(magicNumber).AddCallback(() ->
                                GameActions.Bottom.ChannelOrbs(Frost::new, JUtils.Count(player.orbs, o -> o instanceof EmptyOrbSlot)));
                    }
                }));
        GameActions.Bottom.StackPower(new DelayedDamagePower(p, secondaryValue));
    }
}