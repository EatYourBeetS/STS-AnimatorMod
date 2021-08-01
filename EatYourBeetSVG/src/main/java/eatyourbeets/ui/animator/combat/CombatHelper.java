package eatyourbeets.ui.animator.combat;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class CombatHelper
{
    private static final ArrayList<CreatureStatus> creatures = new ArrayList<>();

    public void Clear()
    {
        creatures.clear();
    }

    public void Update()
    {
        if (creatures.isEmpty())
        {
            return;
        }

        if (GR.UI.Elapsed100() && !GameUtilities.InBattle())
        {
            Clear();
        }
        else for (CreatureStatus s : creatures)
        {
            s.Refresh();
        }
    }

    public static int GetHealthBarAmount(AbstractCreature c, int amount, boolean subtractBlock, boolean subtractTempHP)
    {
        if (c == null || (!subtractBlock && !subtractTempHP))
        {
            return amount;
        }

        CreatureStatus status = null;
        for (CreatureStatus s : creatures)
        {
            if (s.owner == c)
            {
                status = s;
                break;
            }
        }

        if (status == null)
        {
            status = new CreatureStatus(c);
            creatures.add(status);
        }

        if (amount > 0 && subtractBlock)
        {
            int blocked = Mathf.Min(status.block, amount);
            status.block -= blocked;
            amount -= blocked;
        }

        if (amount > 0 && subtractTempHP)
        {
            int blocked = Mathf.Min(status.tempHP, amount);
            status.tempHP -= blocked;
            amount -= blocked;
        }

        return amount > 0 ? amount : 0;
    }

    protected static class CreatureStatus
    {
        public AbstractCreature owner;
        public int block;
        public int tempHP;

        public CreatureStatus(AbstractCreature creature)
        {
            this.owner = creature;
            Refresh();
        }

        public void Refresh()
        {
            tempHP = TempHPField.tempHp.get(owner);
            block = owner.currentBlock;
        }
    }
}
