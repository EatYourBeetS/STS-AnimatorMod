package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Lancer extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Lancer.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    public Lancer()
    {
        super(DATA);

        Initialize(6, 0, 1);
        SetUpgrade(3, 0, 0);
        SetScaling(0, 1, 1);


        SetMartialArtist();
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AbstractGameAction.AttackEffect attackEffect;
        if (this.damage >= 15)
        {
            GameActions.Bottom.VFX(new ClashEffect(m.hb.cX, m.hb.cY), 0.1f);
            attackEffect = AttackEffects.NONE;
        }
        else
        {
            attackEffect = AttackEffects.SLASH_VERTICAL;
        }

        GameActions.Bottom.DealDamage(this, m, attackEffect);
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
    }
}