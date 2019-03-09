package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;

public class SoraEffect_ApplyVulnerable extends SoraEffect
{
    public SoraEffect_ApplyVulnerable(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 2;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            if (!m.isDead && !m.isDying)
            {
                GameActionsHelper.ApplyPower(player, m, new VulnerablePower(m, sora.magicNumber, false), sora.magicNumber);
            }
        }
    }
}