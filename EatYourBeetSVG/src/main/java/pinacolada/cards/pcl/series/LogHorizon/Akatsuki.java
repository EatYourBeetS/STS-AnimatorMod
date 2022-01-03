package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Akatsuki extends PCLCard
{
    public static final PCLCardData DATA = Register(Akatsuki.class)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Akatsuki()
    {
        super(DATA);

        Initialize(11, 0, 2, 4);

        SetAffinity_Green(1, 0, 2);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && !PCLGameUtilities.IsAttacking(enemy.intent))
        {
            amount += secondaryValue;
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.CreateThrowingKnives(magicNumber, player.drawPile)
                    .AddCallback(card ->
                    {
                        if (upgraded)
                        {
                            PCLGameUtilities.SetCardTag(card, HASTE, true);
                        }
                    });
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL)
        .forEach(d -> d.SetDamageEffect(c -> PCLGameEffects.List.Add(new DieDieDieEffect()).duration));

        if (m != null && PCLGameUtilities.IsAttacking(m.intent)) {
            PCLActions.Bottom.CreateThrowingKnives(1);
        }
    }
}