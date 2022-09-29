package eatyourbeets.cards.animator.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class SeriousSaitama extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SeriousSaitama.class)
            .SetSkill(X_COST, CardRarity.SPECIAL, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.OnePunchMan);

    public SeriousSaitama()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(2);
        SetAffinity_Green(2);

        SetPurge(true);
        SetDelayed(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final int x = GameUtilities.UseXCostEnergy(this);
        final int amount = x + magicNumber;

        if (amount > 0)
        {
            GameActions.Bottom.GainForce(amount);
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), amount);
        }

        if (x > 1)
        {
            for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.ApplyPower(p, new StunMonsterPower(enemy, 1));
            }
        }
    }
}