package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class KotoriKanbe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE);
    public static final int HP_HEAL_THRESHOLD = 30;

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);

        SetEthereal(true);
        SetExhaust(true);
        SetSynergy(Synergies.Rewrite);
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
    public void use(AbstractPlayer p, AbstractMonster m)
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