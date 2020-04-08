package eatyourbeets.cards.animator.beta;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.animator.special.Bienfu;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class KotoriKanbe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);
    public static final int HP_HEAL_THRESHOLD = 50;

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0,0,-1, 1);
        SetEthereal(true);
        SetExhaust(true);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int targetMissingHP = m.maxHealth - m.currentHealth;
        int applyAmount = Math.floorDiv(targetMissingHP, magicNumber);

        GameActions.Bottom.ApplyWeak(p, m, applyAmount);
        GameActions.Bottom.ApplyVulnerable(p, m, applyAmount);

        if (targetMissingHP > HP_HEAL_THRESHOLD)
        {
            GameActions.Bottom.ReduceStrength(m, secondaryValue, false);
        }

        GameActions.Bottom.Heal(p, m, targetMissingHP);
    }
}