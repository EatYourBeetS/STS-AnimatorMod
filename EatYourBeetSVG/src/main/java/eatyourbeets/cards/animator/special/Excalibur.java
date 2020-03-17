package eatyourbeets.cards.animator.special;

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

public class Excalibur extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Excalibur.class).SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Excalibur()
    {
        super(DATA);

        Initialize(80, 0);
        SetUpgrade(19, 0);

        SetRetain(true);
        SetExhaust(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, 1));
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));

        for (AbstractCreature m1 : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.VFX(new VerticalImpactEffect(m1.hb_x, m1.hb_y));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
}