package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KotoriKanbe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE).SetSeriesFromClassPackage();
    public static final int HP_HEAL_THRESHOLD = 30;

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);

        SetEthereal(true);
        SetExhaust(true);
        SetAffinity_Light(2, 0, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription(HP_HEAL_THRESHOLD);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            int heal = m.maxHealth - m.currentHealth;
            int stacks = Math.floorDiv(heal, magicNumber);
            if (stacks > 0)
            {
                final EnemyIntent intent = GameUtilities.GetIntent(m).AddWeak();
                if (heal >= HP_HEAL_THRESHOLD && CombatStats.CanActivateLimited(cardID))
                {
                    intent.AddStrength(-secondaryValue);
                }
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int heal = m.maxHealth - m.currentHealth;
        int stacks = Math.floorDiv(heal, magicNumber);

        GameActions.Bottom.Heal(p, m, heal);

        if (stacks > 0)
        {
            GameActions.Bottom.ApplyWeak(p, m, stacks);
            GameActions.Bottom.ApplyVulnerable(p, m, stacks);

            if (heal >= HP_HEAL_THRESHOLD && info.TryActivateLimited())
            {
                GameActions.Bottom.ReduceStrength(m, secondaryValue, false);
            }
        }
    }
}