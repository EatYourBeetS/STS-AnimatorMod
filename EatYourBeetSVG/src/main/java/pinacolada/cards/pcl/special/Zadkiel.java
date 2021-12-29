package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Zadkiel extends PCLCard {
    public static final PCLCardData DATA = Register(Zadkiel.class).SetAttack(2, CardRarity.SPECIAL, PCLAttackType.Ice, eatyourbeets.cards.base.EYBCardTarget.Random).SetSeries(CardSeries.DateALive);

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

        PCLGameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.SMASH).forEach(d -> d
                .SetVFXColor(Color.NAVY)
                .SetDamageEffect(e -> PCLGameEffects.List.Add(VFX.Bite(e.hb, Color.NAVY)).duration)
                .AddCallback(enemy -> {
                    PCLActions.Top.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);
                    if (PCLGameUtilities.IsFatal(enemy, true)) {
                        PCLActions.Bottom.EvokeOrb(magicNumber).AddCallback(() ->
                                PCLActions.Bottom.ChannelOrbs(Frost::new, PCLJUtils.Count(player.orbs, o -> o instanceof EmptyOrbSlot)));
                    }
                }));
        PCLActions.Bottom.StackPower(new DelayedDamagePower(p, secondaryValue));
    }
}