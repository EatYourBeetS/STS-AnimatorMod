package eatyourbeets.monsters.Bosses;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import java.util.Iterator;

public class TheUnnamedMonsterGroup extends MonsterGroup
{
    public TheUnnamedMonsterGroup(AbstractMonster[] input)
    {
        super(input);
    }

    public TheUnnamedMonsterGroup(AbstractMonster m)
    {
        this(new AbstractMonster[]{m});
    }

    @Override
    public void update()
    {
        boolean legit = false;

        Iterator var1 = this.monsters.iterator();
        AbstractMonster m;
        while (var1.hasNext())
        {
            m = (AbstractMonster) var1.next();
            m.update();

            if (m.id.equals(TheUnnamed.ID))
            {
                legit = true;
            }
        }

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.DEATH)
        {
            this.hoveredMonster = null;
            var1 = this.monsters.iterator();

            label43:
            while (true)
            {
                do
                {
                    do
                    {
                        do
                        {
                            if (!var1.hasNext())
                            {
                                break label43;
                            }

                            m = (AbstractMonster) var1.next();
                        }
                        while (m.isDying);
                    }
                    while (m.isEscaping);

                    m.hb.update();
                    m.intentHb.update();
                    m.healthHb.update();
                }
                while (!m.hb.hovered && !m.intentHb.hovered && !m.healthHb.hovered);

                if (!AbstractDungeon.player.isDraggingCard)
                {
                    this.hoveredMonster = m;
                    break;
                }
            }

            if (this.hoveredMonster == null)
            {
                AbstractDungeon.player.hoverEnemyWaitTimer = -1.0F;
            }
        }
        else
        {
            this.hoveredMonster = null;
        }

        if (!legit)
        {
            CardCrawlGame.startOver = true;
        }
    }
}