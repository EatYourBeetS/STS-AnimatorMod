package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class Curse_Destroyer extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Destroyer.class)
            .SetCurse(-2, eatyourbeets.cards.base.EYBCardTarget.None, false).SetSeries(CardSeries.Konosuba);

    public Curse_Destroyer()
    {
        super(DATA, true);
        Initialize(0,0,5,0);
        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetUnique(true, 1);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.StackPower(new DestroyerPower(player, magicNumber));
        PCLActions.Bottom.ShakeScreen(0.25f, ScreenShake.ShakeDur.SHORT, ScreenShake.ShakeIntensity.LOW);

        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.RemovePower(player, player, DestroyerPower.DeriveID(Curse_Destroyer.DATA.ID));
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLActions.Bottom.StackPower(new DestroyerPower(player, magicNumber));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    public static class DestroyerPower extends PCLPower
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

            PCLActions.Bottom.VFX(VFX.VerticalImpact(owner.hb));
            final int[] damage = DamageInfo.createDamageMatrix(amount, true);
            PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SMALL_EXPLOSION)
                    .SetVFX(true, false);
            PCLActions.Bottom.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, amount > 15 ? ScreenShake.ShakeIntensity.HIGH : ScreenShake.ShakeIntensity.MED);
        }
    }
}