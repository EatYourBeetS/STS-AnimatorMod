package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnDamageOverrideSubscriber;
import eatyourbeets.utilities.GameActions;

public class KimizugiMirai extends AnimatorCard implements OnDamageOverrideSubscriber {
    public static final EYBCardData DATA = Register(KimizugiMirai.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public KimizugiMirai() {
        super(DATA);

        Initialize(16, 0, 0);

        SetCostUpgrade(-1);

        SetAffinity_Fire(2);
        SetAffinity_Light(2);
    }

    @Override
    public float OnDamageOverride(AbstractCreature target, DamageInfo.DamageType type, float damage, AbstractCard card)
    {
        if (player.exhaustPile.contains(this))
        {
            return damage * 2;
        }

        return damage;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_BEAM, 0.4f, 0.5f);
        GameActions.Bottom.BorderFlash(Color.YELLOW);
        GameActions.Bottom.VFX(VFX.Mindblast(player.dialogX, player.dialogY), 0.1f);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.PSYCHOKINESIS);
    }
}