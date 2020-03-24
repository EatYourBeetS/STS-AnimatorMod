package eatyourbeets.monsters.UnnamedReign.UnnamedCultist.Moveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.utilities.JavaUtilities;

public class Move_SummonEnemy extends AbstractMove
{
    private int summonCount = 0;
    private TheUnnamed_Cultist cultist;
    private AbstractMonster summon;

    @Override
    public void Init(byte id, AbstractMonster owner)
    {
        super.Init(id, owner);

        cultist = (TheUnnamed_Cultist)owner;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return (summonCount == 0 || !(summon instanceof TheUnnamed_Doll));
    }

    public void SetSummon(AbstractMonster monster)
    {
        summon = monster;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        summonCount += 1;

        if (summon.id.equals(TheUnnamed_Doll.ID))
        {
            GameActions.Bottom.Talk(owner, cultist.data.strings.DIALOG[1], 0.5f, 2f);
            GameActions.Bottom.WaitRealtime(2f);
            GameActions.Bottom.Add(new SummonMonsterAction(summon, true));
            return;
        }

        if (summon.id.equals(ShelledParasite.ID))
        {
            GameActions.Bottom.Talk(owner, cultist.data.strings.DIALOG[4], 3f, 3f);
        }
        else if (summon.id.equals(GremlinWarrior.ID))
        {
            GameActions.Bottom.Talk(owner, cultist.data.strings.DIALOG[3], 3f, 3f);
        }
        else
        {
            GameActions.Bottom.Talk(owner, JavaUtilities.Format(cultist.data.strings.DIALOG[2], summon.name), 3f, 3f);
        }

        GameActions.Bottom.Add(new SummonMonsterAction(summon, false));
    }
}
