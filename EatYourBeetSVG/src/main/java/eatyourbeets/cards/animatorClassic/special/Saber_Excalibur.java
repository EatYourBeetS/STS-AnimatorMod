package eatyourbeets.cards.animatorClassic.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Saber_Excalibur extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Saber_Excalibur.class).SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Saber_Excalibur()
    {
        super(DATA);

        Initialize(80, 0);
        SetUpgrade(19, 0);

        SetRetain(true);
        SetExhaust(true);
        this.series = CardSeries.Fate;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, 1));
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));

        for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(new VerticalImpactEffect(m1.hb_x, m1.hb_y));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
}