package eatyourbeets.cards.animator.colorless.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Canti extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Canti.class).SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS);

    public Canti()
    {
        super(DATA);

        Initialize(2, 3);
        SetUpgrade(4, 0);

        SetSynergy(Synergies.FLCL);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && GameUtilities.IsAttacking(enemy.intent))
        {
            amount += enemy.getIntentDmg();
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (damage >= 20)
        {
            //GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY));
            //GameActions.Bottom.Wait(0.8F);
            GameActions.Bottom.VFX(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.SKY));
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
    }
}