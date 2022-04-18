package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

public class KotoriKanbe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class)
            .SetSkill(1, CardRarity.RARE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Rewrite);
    public static final int HP_HEAL_STEP = 2;
    public static final int STRENGTH_REDUCTION = 3;

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 12, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            final EnemyIntent intent = GameUtilities.GetIntent(m);
            if (heal < (m.maxHealth - m.currentHealth) && CombatStats.CanActivateLimited(cardID))
            {
                intent.AddStrength(-STRENGTH_REDUCTION);
            }

            intent.AddWeak();
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCardHeal(this);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        heal = GetHealAmount(enemy);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyWeak(player, m, secondaryValue);
        GameActions.Bottom.ApplyVulnerable(player, m, secondaryValue);
        GameActions.Bottom.Heal(p, m, GetHealAmount(m))
        .AddCallback(m.currentHealth, (previousHealth, info2) ->
        {
            final AbstractCreature target = info2.V1;
            if (target.currentHealth < target.maxHealth && CombatStats.TryActivateLimited(cardID))
            {
                GameActions.Bottom.ReduceStrength(target, STRENGTH_REDUCTION, false);
            }
        });
    }

    protected int GetHealAmount(AbstractMonster m)
    {
        return m == null ? 0 : Mathf.CeilToInt(m.maxHealth * magicNumber * 0.01f);
    }
}