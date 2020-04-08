package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class KotoriKanbe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);
    public static final int HP_HEAL_THRESHOLD = 40;

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, -1);

        SetEthereal(true);
        SetExhaust(true);
        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int missingHP = m.maxHealth - m.currentHealth;
        int amount = Math.floorDiv(missingHP, magicNumber);
        if (amount > 0)
        {
            GameActions.Bottom.ApplyWeak(p, m, amount);
            GameActions.Bottom.ApplyVulnerable(p, m, amount);

            if (missingHP > HP_HEAL_THRESHOLD)
            {
                GameActions.Bottom.ReduceStrength(m, secondaryValue, false);
            }

            GameActions.Bottom.Heal(p, m, missingHP);
        }
    }
}