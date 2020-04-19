import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.AdvancedHitbox;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.utilities.InputManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class EYBModInitializer implements OnStartBattleSubscriber, PostBattleSubscriber, PreMonsterTurnSubscriber,
                                          PostEnergyRechargeSubscriber, PostDrawSubscriber, PostDeathSubscriber,
                                          PreStartGameSubscriber, PostUpdateSubscriber, PostRenderSubscriber
{
    private static final Logger logger = LogManager.getLogger(EYBModInitializer.class.getName());
    private static GUI_TextBox testModeLabel;

    public static void initialize()
    {
        logger.info("EYBModInitializer()");
        BaseMod.subscribe(new EYBModInitializer());
        GR.Initialize();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom)
    {
        CombatStats.EnsurePowerIsApplied();
        CombatStats.Instance.OnBattleStart();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom)
    {
        CombatStats.Instance.OnBattleEnd();
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard)
    {
        CombatStats.Instance.OnAfterDraw(abstractCard);
    }

    @Override
    public void receivePostEnergyRecharge()
    {
        CombatStats.EnsurePowerIsApplied(); // Ensure PlayerStatistics is always active at turn start
    }

    @Override // false = skips monster turn
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster)
    {
        CombatStats.EnsurePowerIsApplied();

        return true;
    }

    @Override
    public void receivePreStartGame()
    {
        CombatStats.OnGameStart();
    }

    @Override
    public void receivePostDeath()
    {
        CombatStats.OnAfterDeath();
    }

    @Override
    public void receivePostUpdate()
    {
        InputManager.PostUpdate();
    }

    @Override
    public void receivePostRender(SpriteBatch sb)
    {
        if (GR.TEST_MODE)
        {
            if (testModeLabel == null)
            {
                testModeLabel = new GUI_TextBox(GR.Common.Images.Panel.Texture(),
                        new AdvancedHitbox(Settings.WIDTH * 0.12f, Settings.HEIGHT * 0.08f))
                        .SetPosition(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.85f)
                        .SetAlignment(0.5f, 0.5f)
                        .SetText("TEST MODE");
            }

            testModeLabel.Render(sb);
        }
    }
}