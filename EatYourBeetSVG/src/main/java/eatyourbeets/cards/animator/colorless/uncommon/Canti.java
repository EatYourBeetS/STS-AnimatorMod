package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
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

        Initialize(5, 3);
        SetUpgrade(2, 1);

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

        if (damage > 15)
        {
            GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY));
            GameActions.Bottom.Wait(0.8F);
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
    }
}