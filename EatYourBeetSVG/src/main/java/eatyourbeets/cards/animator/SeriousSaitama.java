package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class SeriousSaitama extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(SeriousSaitama.class.getSimpleName());

    public SeriousSaitama()
    {
        super(ID, 3, CardType.SKILL, CardTarget.ALL);

        Initialize(0, 0);

        this.exhaust = true;

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        RandomizedList<AbstractMonster> minion = new RandomizedList<>();
        RandomizedList<AbstractMonster> normal = new RandomizedList<>();
        RandomizedList<AbstractMonster> elite = new RandomizedList<>();

        ArrayList<AbstractMonster> enemies = PlayerStatistics.GetCurrentEnemies(true);
        for (AbstractMonster m1 : enemies)
        {
            if (m1.hasPower(MinionPower.POWER_ID))
            {
                minion.Add(m1);
            }
            else if (m1.type == AbstractMonster.EnemyType.NORMAL)
            {
                normal.Add(m1);
            }
            else if (m1.type == AbstractMonster.EnemyType.ELITE)
            {
                elite.Add(m1);
            }

            if (!m1.hasPower(StunMonsterPower.POWER_ID))
            {
                GameActionsHelper.ApplyPower(p, m1, new StunMonsterPower(m1, 1), 1);
            }
        }

        AbstractMonster enemy;
        if (minion.Count() > 0)
        {
            enemy = minion.Retrieve(AbstractDungeon.miscRng);
        }
        else if (normal.Count() > 0)
        {
            enemy = normal.Retrieve(AbstractDungeon.miscRng);
        }
        else
        {
            enemy = elite.Retrieve(AbstractDungeon.miscRng);
        }

        if (enemies.size() > 1 && enemy != null)
        {
            GameActionsHelper.AddToBottom(new EscapeAction(enemy));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.isInnate = true;
        }
    }
}