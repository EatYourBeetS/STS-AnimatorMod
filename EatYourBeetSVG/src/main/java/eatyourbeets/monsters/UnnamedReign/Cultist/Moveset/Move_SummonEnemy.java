package eatyourbeets.monsters.UnnamedReign.Cultist.Moveset;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.SummonMonsterAction;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.UnnamedReign.Cultist.TheUnnamed_Cultist;
import eatyourbeets.monsters.UnnamedReign.Cultist.TheUnnamed_Cultist_Single;
import javafx.util.Pair;

public class Move_SummonEnemy extends AbstractMove
{
    private TheUnnamed_Cultist_Single cultist;
    private AbstractMonster summon;

    public Move_SummonEnemy(int id, int ascensionLevel, TheUnnamed_Cultist_Single owner)
    {
        super((byte) id, ascensionLevel, owner);

        cultist = owner;
    }

    public void SetSummon(AbstractMonster monster)
    {
        summon = monster;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return false;
    }

    public void Execute(AbstractPlayer target)
    {
        if (summon.id.equals(ShelledParasite.ID))
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, cultist.strings.DIALOG[4], 3f, 3f));
        }
        else if (summon.id.equals(GremlinWarrior.ID))
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, cultist.strings.DIALOG[3], 3f, 3f));
        }
        else
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, cultist.strings.DIALOG[2].replace("@", summon.name), 3f, 3f));
        }

        GameActionsHelper.AddToBottom(new SummonMonsterAction(summon, false));
    }

    public AbstractMonster GetSummon1()
    {
        if (cultist.pool1.Count() > 0)
        {
            if (cultist.firstSummon == null || cultist.firstSummon.isDeadOrEscaped())
            {
                return cultist.pool1.Retrieve(AbstractDungeon.aiRng);
            }
        }

        return null;
    }

    public AbstractMonster GetSummon2()
    {
        if (cultist.pool2.Count() > 0)
        {
            if (cultist.firstSummon == null || cultist.firstSummon.isDeadOrEscaped())
            {
                return cultist.pool2.Retrieve(AbstractDungeon.aiRng);
            }
        }

        return null;
    }
}
