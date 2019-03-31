import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.characters.AnimatorMetrics;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.CursedBlade;
import eatyourbeets.relics.PurgingStone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

import java.util.ArrayList;

@SpireInitializer
public class EYBModInitializer
        implements EditCharactersSubscriber, EditStringsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber,
                   OnStartBattleSubscriber, PostBattleSubscriber, PreMonsterTurnSubscriber, PostInitializeSubscriber, PostEnergyRechargeSubscriber,
                   PostDrawSubscriber, StartGameSubscriber, StartActSubscriber, MaxHPChangeSubscriber, AddAudioSubscriber
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
        Color color = CardHelper.getColor(210, 147, 106);
        BaseMod.addColor(AbstractEnums.Cards.THE_ANIMATOR, color, color, color, color, color, color, color,
                AnimatorResources.ATTACK_PNG, AnimatorResources.SKILL_PNG , AnimatorResources.POWER_PNG ,
                AnimatorResources.ORB_A_PNG , AnimatorResources.ATTACK_P_PNG , AnimatorResources.SKILL_P_PNG ,
                AnimatorResources.POWER_P_PNG, AnimatorResources.ORB_B_PNG , AnimatorResources.ORB_C_PNG);
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
        logger.info("Drawn: " + abstractCard.name);
        PlayerStatistics.Instance.OnAfterDraw(abstractCard);
    }

    @Override
    public void receivePostEnergyRecharge()
    {
        // Ensure PlayerStatistics is always active at turn start
        PlayerStatistics.EnsurePowerIsApplied();
    }

    @Override // false = skips monster turn
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster)
    {
        PlayerStatistics.EnsurePowerIsApplied();

        return true;
    }

    @Override
    public void receiveEditCharacters()
    {
        AnimatorCharacter animatorCharacter = new AnimatorCharacter(AnimatorCharacter.NAME, AbstractEnums.Characters.THE_ANIMATOR);
        BaseMod.addCharacter(animatorCharacter, AnimatorResources.CHAR_BUTTON_PNG, AnimatorResources.CHAR_PORTRAIT_JPG, AbstractEnums.Characters.THE_ANIMATOR);
    }

    @Override
    public void receiveEditStrings()
    {
        AnimatorResources.LoadGameStrings();
    }

    @Override
    public void receiveEditKeywords()
    {
        AnimatorResources.LoadCustomKeywords();
    }

    @Override
    public void receiveEditRelics()
    {
        AnimatorResources.LoadCustomRelics();
    }

    @Override
    public void receiveEditCards()
    {
        try
        {
            AnimatorResources.LoadCustomEvents();
            AnimatorResources.LoadCustomCards();
        }
        catch (Exception e)
        {
            logger.error(e);
        }

        if (Loader.DEBUG)
        {
            if (AnimatorResources.CreateDebugFile())
            {
                logger.info("Created Debug File at c:\\temp");
            }
        }
    }

    @Override
    public void receivePostInitialize()
    {
        AnimatorResources.LoadCustomRewards();
    }

    @Override
    public void receiveStartGame()
    {
        if (Settings.isStandardRun())
        {
            if (AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR)
            {
                AnimatorMetrics.SaveTrophies(true);
            }
            else
            {
                RemoveColorless(AbstractDungeon.srcColorlessCardPool);
                RemoveColorless(AbstractDungeon.colorlessCardPool);
            }
        }

        PurgingStone purgingStone = PurgingStone.GetInstance();
        if (purgingStone != null)
        {
            purgingStone.UpdateBannedCards();
        }
    }

    @Override
    public void receiveStartAct()
    {
        if (AbstractDungeon.player.chosenClass != AbstractEnums.Characters.THE_ANIMATOR)
        {
            RemoveColorless(AbstractDungeon.srcColorlessCardPool);
            RemoveColorless(AbstractDungeon.colorlessCardPool);
        }

        PurgingStone purgingStone = PurgingStone.GetInstance();
        if (purgingStone != null)
        {
            purgingStone.UpdateBannedCards();
        }
    }

    @Override
    public int receiveMaxHPChange(int amount)
    {
        if (amount > 0 && AbstractDungeon.player.hasRelic(CursedBlade.ID))
        {
            return Math.max(1, amount / 2);
        }

        return amount;
    }

    @Override
    public void receiveAddAudio()
    {
        AnimatorResources.LoadAudio();
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
}