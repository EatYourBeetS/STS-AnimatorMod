import basemod.BaseMod;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.console.CommandsManager;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.animator.AbstractPurgingStone;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.InputManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SpireInitializer
public class EYBModInitializer implements OnStartBattleSubscriber, PostBattleSubscriber, PreMonsterTurnSubscriber,
                                           PostEnergyRechargeSubscriber, PostDrawSubscriber, StartGameSubscriber,
                                           StartActSubscriber, MaxHPChangeSubscriber, PostDeathSubscriber,
                                           PreStartGameSubscriber, PreUpdateSubscriber, PostUpdateSubscriber, PostInitializeSubscriber
{
    private static final Logger logger = LogManager.getLogger(EYBModInitializer.class.getName());

    public static void initialize()
    {
        // Entry Point
        new EYBModInitializer();
    }

    private EYBModInitializer()
    {
        logger.info("EYBModInitializer()");

        BaseMod.subscribe(this);
        GR.Initialize();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom)
    {
        PlayerStatistics.EnsurePowerIsApplied();
        PlayerStatistics.Instance.OnBattleStart();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom)
    {
        PlayerStatistics.Instance.OnBattleEnd();
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard)
    {
        PlayerStatistics.Instance.OnAfterDraw(abstractCard);
    }

    @Override
    public void receivePostEnergyRecharge()
    {
        PlayerStatistics.EnsurePowerIsApplied(); // Ensure PlayerStatistics is always active at turn start
    }

    @Override
    public void receivePostInitialize()
    {
        CommandsManager.RegisterCommands();
    }

    @Override // false = skips monster turn
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster)
    {
        PlayerStatistics.EnsurePowerIsApplied();

        return true;
    }

    @Override
    public void receivePreStartGame()
    {
        PlayerStatistics.OnGameStart();
    }

    @Override
    public void receiveStartGame()
    {
        if (AbstractDungeon.player.chosenClass == GR.Animator.PlayerClass)
        {
            if (Settings.isStandardRun())
            {
                GR.Animator.Database.SaveTrophies(true);
            }

            AbstractPurgingStone.UpdateBannedCards();
        }
        else
        {
            RemoveColorless(AbstractDungeon.srcColorlessCardPool);
            RemoveColorless(AbstractDungeon.colorlessCardPool);
        }
    }

    @Override
    public void receiveStartAct()
    {
        if (AbstractDungeon.player.chosenClass != GR.Enums.Characters.THE_ANIMATOR)
        {
            RemoveColorless(AbstractDungeon.srcColorlessCardPool);
            RemoveColorless(AbstractDungeon.colorlessCardPool);
        }

        AbstractPurgingStone.UpdateBannedCards();
    }

    private void RemoveColorless(CardGroup group)
    {
        ArrayList<AbstractCard> toRemove = new ArrayList<>();
        for (AbstractCard c : group.group)
        {
            if (c instanceof AnimatorCard)
            {
                toRemove.add(c);
                logger.info("REMOVING: " + c.name);
            }
        }

        for (AbstractCard c : toRemove)
        {
            group.removeCard(c);
        }
    }

    @Override
    public void receivePostDeath()
    {
        PlayerStatistics.OnAfterDeath();
    }

    @Override
    public void receivePostUpdate()
    {
        InputManager.PostUpdate();
    }

    @Override
    public void receivePreUpdate()
    {
        InputManager.PreUpdate();
    }
}