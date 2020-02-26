package eatyourbeets.cards.animator.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SeriousSaitama extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SeriousSaitama.class).SetSkill(-1, CardRarity.SPECIAL, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);

    public SeriousSaitama()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 1);

        SetPurge(true);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int amount = GameUtilities.UseXCostEnergy(this) + magicNumber;
        if (amount > 0)
        {
            GameActions.Bottom.GainForce(amount);
            GameActions.Bottom.GainStrength(amount);
        }

        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            if (!enemy.hasPower(StunMonsterPower.POWER_ID))
            {
                GameActions.Bottom.ApplyPower(p, enemy, new StunMonsterPower(enemy, 1), 1);
            }
        }
    }
}