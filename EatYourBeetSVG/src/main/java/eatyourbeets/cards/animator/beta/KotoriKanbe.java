package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class KotoriKanbe extends AnimatorCard {
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(2, CardRarity.RARE).SetColor(CardColor.COLORLESS);
    private static final int HEAL_AMOUNT = 35;

    public KotoriKanbe() {
        super(DATA);

        Initialize(0, 0, 5, 5);
        SetUpgrade(0, 0, 1, 1);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.RemovePower(p, m, ArtifactPower.POWER_ID);
        GameActions.Bottom.ApplyWeak(p, m, magicNumber);
        GameActions.Bottom.ApplyVulnerable(p, m, secondaryValue);
        GameActions.Bottom.Heal(p, m, HEAL_AMOUNT);
    }
}