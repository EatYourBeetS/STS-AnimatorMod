package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Zadkiel extends AnimatorCard {
    public static final EYBCardData DATA = Register(Zadkiel.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.Random).SetSeries(CardSeries.DateALive);

    public Zadkiel() {
        super(DATA);

        Initialize(36, 0, 2, 9);
        SetUpgrade(15, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.SMASH)
                .SetDamageEffect(e -> GameEffects.List.Add(VFX.Bite(e.hb, Color.NAVY)).duration)
                .AddCallback(enemy -> {
                    if (GameUtilities.IsFatal(enemy, true)) {
                        GameActions.Bottom.EvokeOrb(magicNumber).AddCallback(() ->
                                GameActions.Bottom.ChannelOrbs(Frost::new, JUtils.Count(player.orbs, o -> o instanceof EmptyOrbSlot)));
                    }
                });
        GameActions.Bottom.StackPower(new SelfDamagePower(p, secondaryValue));
    }
}