package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import eatyourbeets.cards.animator.beta.status.Frostbite;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Ganyu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ganyu.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);
    public static final int CARD_COUNT = 5;

    public Ganyu()
    {
        super(DATA);

        Initialize(2, 0, 30, 3);
        SetUpgrade(1, 0, 8);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Freezing, secondaryValue);
        }
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && GameUtilities.GetPowerAmount(enemy, LockOnPower.POWER_ID) >= 1)
        {
            amount += magicNumber;
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (GameUtilities.GetPowerAmount(m, LockOnPower.POWER_ID) >= 1) {
            GameActions.Bottom.VFX(new ClawEffect(m.hb.cX, m.hb.cY, Color.TEAL, Color.WHITE));
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
        else {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }
        for (int i = 0; i < CARD_COUNT; i++) {
            GameActions.Bottom.MakeCardInDrawPile(new Frostbite())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }
}