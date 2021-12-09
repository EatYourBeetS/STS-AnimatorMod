package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Curse_Destroyer extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Destroyer.class)
            .SetCurse(-2, EYBCardTarget.None, false).SetSeries(CardSeries.Konosuba);

    public Curse_Destroyer()
    {
        super(DATA, true);
        Initialize(0,0,5,0);
        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetUnique(true, false);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.StackPower(new DestroyerPower(player, magicNumber));
        GameActions.Bottom.ShakeScreen(0.25f, ScreenShake.ShakeDur.SHORT, ScreenShake.ShakeIntensity.LOW);

        GameActions.Bottom.Flash(this);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.RemovePower(player, player, DestroyerPower.DeriveID(Curse_Destroyer.DATA.ID));
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        GameActions.Bottom.StackPower(new DestroyerPower(player, magicNumber));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    public static class DestroyerPower extends AnimatorPower
    {

        public DestroyerPower(AbstractPlayer owner, int amount)
        {
            super(owner, Curse_Destroyer.DATA);

            Initialize(amount, PowerType.DEBUFF, false);
        }

        @Override
        public void atEndOfRound()
        {
            super.atEndOfRound();

            GameActions.Bottom.VFX(VFX.VerticalImpact(owner.hb));
            final int[] damage = DamageInfo.createDamageMatrix(amount, true);
            GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SMALL_EXPLOSION)
                    .SetVFX(true, false);
            GameActions.Bottom.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, amount > 15 ? ScreenShake.ShakeIntensity.HIGH : ScreenShake.ShakeIntensity.MED);
        }
    }
}