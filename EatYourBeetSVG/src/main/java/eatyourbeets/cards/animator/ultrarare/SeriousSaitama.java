package eatyourbeets.cards.animator.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class SeriousSaitama extends AnimatorCard_UltraRare
{
    public static final String ID = Register(SeriousSaitama.class);

    public SeriousSaitama()
    {
        super(ID, -1, CardType.SKILL, CardTarget.ALL);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetPurge(true);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int amount = GameUtilities.UseEnergyXCost(this);
        if (upgraded)
        {
            amount += 1;
        }
        if (amount > 0)
        {
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