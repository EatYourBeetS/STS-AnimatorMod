package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;
import patches.AbstractEnums;

import java.util.ArrayList;

public class SeriousSaitama extends AnimatorCard_UltraRare
{
    public static final String ID = Register(SeriousSaitama.class.getSimpleName());

    public SeriousSaitama()
    {
        super(ID, -1, CardType.SKILL, CardTarget.ALL);

        Initialize(0, 0);

        SetPurge(true);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            if (!enemy.hasPower(StunMonsterPower.POWER_ID))
            {
                GameActions.Bottom.ApplyPower(p, enemy, new StunMonsterPower(enemy, 1), 1);
            }
        }

        if (this.energyOnUse < EnergyPanel.totalCount)
        {
            this.energyOnUse = EnergyPanel.totalCount;
        }

        int amount = energyOnUse;
        if (upgraded)
        {
            amount += 1;
        }

        if (p.hasRelic(ChemicalX.ID))
        {
            amount += ChemicalX.BOOST;
        }

        if (!this.freeToPlayOnce)
        {
            p.energy.use(EnergyPanel.totalCount);
        }

        if (amount > 0)
        {
            GameActions.Bottom.GainStrength(amount);
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}