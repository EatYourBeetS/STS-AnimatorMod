package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Ganyu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ganyu.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);

    public Ganyu()
    {
        super(DATA);

        Initialize(33, 0, 2, 5);
        SetUpgrade(9, 0, 0);
        SetAffinity_Blue(2, 0, 1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber);
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            if (m == null)
            {
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
                {
                    if (GameUtilities.GetPowerAmount(enemy,LockOnPower.POWER_ID) >= 1)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return GameUtilities.GetPowerAmount(m,LockOnPower.POWER_ID) >= 1;
            }
        }

        return false;
    }
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new ClawEffect(m.hb.cX, m.hb.cY, Color.TEAL, Color.WHITE));
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Player(), magicNumber);
        for (int i = 0; i < secondaryValue; i++) {
            GameActions.Bottom.MakeCardInDrawPile(new Wound())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }
}