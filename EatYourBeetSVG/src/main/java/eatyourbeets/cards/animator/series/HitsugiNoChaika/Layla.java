package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

@SuppressWarnings("SuspiciousNameCombination")
public class Layla extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Layla.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Layla()
    {
        super(DATA);

        Initialize(7, 0, 2, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1, 0, 2);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + (GameUtilities.GetDebuffsCount(enemy) * secondaryValue));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                final AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);
                if (enemy != null)
                {
                    GameActions.Bottom.VFX(new PotionBounceEffect(player.hb.cY, player.hb.cX, enemy.hb.cX, enemy.hb.cY), 0.3f);
                }

                GameActions.Bottom.Add(new BouncingFlaskAction(enemy, magicNumber, cards.size()));
            }
        });
    }
}