package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class JumpyDumpty extends AnimatorCard
{
    public static final EYBCardData DATA = Register(JumpyDumpty.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Elemental);

    public JumpyDumpty()
    {
        super(DATA);

        Initialize(9, 0, 1);
        SetUpgrade(3, 0, 1);
        SetAutoplay(true);
        SetExhaust(true);
        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
                .AddCallback(e ->
                {
                    int selfDamage = this.damage - e.lastDamageTaken;
                    if (selfDamage > 0) {
                        GameActions.Bottom.DealDamage(null, player, selfDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
                    }

                });

        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
    }
}