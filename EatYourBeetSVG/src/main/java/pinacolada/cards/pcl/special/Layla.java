package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Layla extends PCLCard
{
    public static final PCLCardData DATA = Register(Layla.class)
            .SetAttack(2, CardRarity.SPECIAL, PCLAttackType.Piercing)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika);

    public Layla()
    {
        super(DATA);

        Initialize(7, 0, 2, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Green(1, 0 ,1);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + (PCLGameUtilities.GetDebuffsCount(enemy) * secondaryValue));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        PCLActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                final AbstractMonster enemy = PCLGameUtilities.GetRandomEnemy(true);
                if (enemy != null)
                {
                    PCLActions.Bottom.VFX(new PotionBounceEffect(player.hb.cY, player.hb.cX, enemy.hb.cX, enemy.hb.cY), 0.3f);
                }

                PCLActions.Bottom.Add(new BouncingFlaskAction(enemy, magicNumber, cards.size()));
            }
        });
    }
}