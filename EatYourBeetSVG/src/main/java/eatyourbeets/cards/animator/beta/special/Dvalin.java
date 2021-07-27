package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Dvalin extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(Dvalin.class).SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Normal).SetSeries(CardSeries.GenshinImpact);

    public Dvalin()
    {
        super(DATA);

        Initialize(25, 0, 10);
        SetUpgrade(7, 0, 0);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Red(1, 0, 1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new ClawEffect(m.hb.cX, m.hb.cY, Color.TEAL, Color.WHITE));
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY).AddCallback(target -> {
            if (GameUtilities.IsDeadOrEscaped(target)) {
                AbstractMonster newTarget = GameUtilities.GetRandomEnemy(true);
                GameActions.Bottom.VFX(new ClawEffect(newTarget.hb.cX, newTarget.hb.cY, Color.TEAL, Color.WHITE));
                GameActions.Bottom.DealDamage(player, newTarget, this.damage + magicNumber, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            }
            else {
                GameActions.Bottom.VFX(new ClawEffect(target.hb.cX, target.hb.cY, Color.TEAL, Color.WHITE));
                GameActions.Bottom.DealDamage(this, target, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            }
        });
        GameActions.Top.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
    }
}