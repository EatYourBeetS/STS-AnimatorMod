package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import patches.AbstractEnums;

import java.util.ArrayList;

public class SeriousSaitama extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(SeriousSaitama.class.getSimpleName());

    public SeriousSaitama()
    {
        super(ID, -1, CardType.SKILL, CardTarget.ALL);

        Initialize(0, 0);

        this.tags.add(AbstractEnums.CardTags.PURGE);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ArrayList<AbstractMonster> enemies = PlayerStatistics.GetCurrentEnemies(true);
        for (AbstractMonster m1 : enemies)
        {
            if (!m1.hasPower(StunMonsterPower.POWER_ID))
            {
                GameActionsHelper.ApplyPower(p, m1, new StunMonsterPower(m1, 1), 1);
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

        if (!this.freeToPlayOnce)
        {
            p.energy.use(EnergyPanel.totalCount);
        }

        if (amount > 0)
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, amount), amount);
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}