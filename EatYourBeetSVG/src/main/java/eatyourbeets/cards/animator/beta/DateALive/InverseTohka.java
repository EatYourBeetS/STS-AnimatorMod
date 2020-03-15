package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class InverseTohka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(InverseTohka.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL);
    public static final int AUTO_ENERGY_COST = 1;

    public InverseTohka()
    {
        super(DATA);

        Initialize(10, 0, 10);
        SetScaling(2, 0, 2);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.SpendEnergy(AUTO_ENERGY_COST, false)
        .AddCallback(amount ->
        {
            int[] damageMatrix = DamageInfo.createDamageMatrix(magicNumber, true);
            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect((enemy, __) -> GameActions.Bottom.VFX(new DarkOrbActivateEffect(enemy.hb_x, enemy.hb_y)));

            GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Reload(name, cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).SetVFX(true, false);
            }
        });

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.HIGH));
    }
}