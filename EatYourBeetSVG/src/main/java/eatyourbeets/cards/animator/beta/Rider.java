package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rider extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(Rider.class).SetSkill(1, CardRarity.COMMON);

    public Rider()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 0);
        SetScaling(0, 1, 0);

        SetPiercing(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        if (enemy != null)
        {
            damage += (damage * (1 - GameUtilities.GetHealthPercentage(enemy)));
        }

        return super.ModifyDamage(enemy, damage);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractGameAction.AttackEffect attackEffect;
        if (this.damage >= 15)
        {
            GameActions.Bottom.VFX(new ClashEffect(m.hb.cX, m.hb.cY), 0.1F);
            attackEffect = AbstractGameAction.AttackEffect.NONE;
        }
        else
        {
            attackEffect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
        }

        GameActions.Bottom.DealDamage(this, m, attackEffect);
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
    }
}