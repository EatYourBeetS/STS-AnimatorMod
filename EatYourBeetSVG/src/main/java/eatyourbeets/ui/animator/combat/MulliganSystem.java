package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.*;

public class MulliganSystem extends GUIElement implements OnStartOfTurnPostDrawSubscriber
{
    protected static final String KEY = GR.Common.CreateID("Mulligan");
    protected static final int MAX_USES = 2;

    protected final EYBCardTooltip tooltip;
    protected final GUI_Button button;
    protected int startOfCombatUses;
    protected int usesPerTurn;
    protected int uses;

    public MulliganSystem()
    {
        SetActive(false);

        tooltip = new EYBCardTooltip(GR.Animator.Strings.Misc.MulliganHeader, GR.Animator.Strings.Misc.MulliganDescription);
        button = new GUI_Button(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new AdvancedHitbox(0, 0, Scale(120), Scale(66)))
        .SetForeground(GR.Common.Images.Panel_Rounded_Half_H_Border.Texture(), Color.DARK_GRAY)
        .SetOnClick(() -> AbstractDungeon.player.useJumpAnimation())
        .SetColor(new Color(0.35f, 0.3f, 0.3f, 1f))
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 1.3f)
        .SetNonInteractableTextColor(null)
        .SetTooltip(tooltip, false)
        .SetClickDelay(1f);
    }

    public void Clear()
    {
        this.uses = 0;
        this.usesPerTurn = 0;
        this.startOfCombatUses = MAX_USES;
        this.SetActive(true);

        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    public void AddUsesPerTurn(int uses)
    {
        this.uses = Math.max(0, this.uses + uses);
        this.usesPerTurn += uses;
        SetActive(this.uses > 0);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        this.uses = usesPerTurn;
        SetActive(this.uses > 0);
    }

    @Override
    public void Update()
    {
        final AbstractPlayer player = AbstractDungeon.player;
        final boolean startOfCombat = CombatStats.TurnCount(true) == 0 && CombatStats.CanActivatedStarter();
        if (player == null || (startOfCombat ? startOfCombatUses <= 0 : uses <= 0))
        {
            SetActive(false);
            return;
        }

        final int delayedDamage = startOfCombat ? (startOfCombatUses < MAX_USES ? 2 : 1) : 0;
        final DrawPilePanel panel = AbstractDungeon.overlayMenu.combatDeckPanel;
        button.SetPosition(panel.current_x + Scale(70), panel.current_y + Scale(110) + (button.hb.height * 0.5f))
        .SetInteractable(!AbstractDungeon.isScreenUp && AbstractDungeon.actionManager.phase == GameActionManager.Phase.WAITING_ON_USER)
        .SetText(GetButtonText(startOfCombat, delayedDamage), true)
        //.SetColor(Testing.TryGetColor(button.buttonColor))
        .Update();

        if (button.interactable && player.hoveredCard != null && !player.isDraggingCard && InputManager.RightClick.IsJustPressed())
        {
            if (delayedDamage > 0)
            {
                GameActions.Bottom.TakeDamageAtEndOfTurn(delayedDamage).ShowEffect(false, false);
            }

            if (startOfCombat)
            {
                startOfCombatUses -= 1;
            }
            else
            {
                uses -= 1;
            }

            GameActions.Bottom.MoveCard(player.hoveredCard, player.drawPile)
            .SetDestination(CardSelection.Bottom(GameUtilities.GetRNG().random((int)(player.drawPile.size() * 0.8f))))
            .SetDuration(0.1f, false);
            GameActions.Bottom.Draw(1);
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        button.Render(sb);
    }

    protected String GetButtonText(boolean startOfCombat, int damage)
    {
        return (startOfCombat ? (startOfCombatUses + "/" + MAX_USES) : (uses + "/" + usesPerTurn)) + " (" + damage + " [DD])";
    }
}
