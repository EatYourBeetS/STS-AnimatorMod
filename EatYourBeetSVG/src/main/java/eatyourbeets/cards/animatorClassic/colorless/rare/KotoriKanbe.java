package eatyourbeets.cards.animatorClassic.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KotoriKanbe extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);
    public static final int HP_HEAL_THRESHOLD = 30;

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);

        SetEthereal(true);
        SetExhaust(true);
        SetSeries(CardSeries.Rewrite);
    }

    @Override
    public String GetRawDescription()
    {
        return super.GetRawDescription(HP_HEAL_THRESHOLD);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            int heal = m.maxHealth - m.currentHealth;
            int stacks = Math.floorDiv(heal, magicNumber);
            if (stacks > 0)
            {
                EnemyIntent intent = GameUtilities.GetIntent(m).AddWeak();
                if (heal >= HP_HEAL_THRESHOLD && !CombatStats.HasActivatedLimited(cardID))
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

            if (heal >= HP_HEAL_THRESHOLD && CombatStats.TryActivateLimited(cardID))
            {
                GameActions.Bottom.ReduceStrength(m, secondaryValue, false);
            }
        }
    }
}