package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class TacticalRetreat extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(TacticalRetreat.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public TacticalRetreat()
    {
        super(DATA);

        Initialize(0, 2);
        SetUpgrade(0, 3);

        SetRetain(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        CombatStats.onStartOfTurnPostDraw.SubscribeOnce(this);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameActions.Last.Callback(() ->
        {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !GameUtilities.AreMonstersBasicallyDead())
            {
                GameEffects.List.Add(new SmokeBombEffect(player.hb.cX, player.hb.cY));
                AbstractDungeon.getCurrRoom().smoked = true;
                AbstractDungeon.player.hideHealthBar();
                AbstractDungeon.player.isEscaping = true;
                AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
                AbstractDungeon.overlayMenu.endTurnButton.disable();
                AbstractDungeon.player.escapeTimer = 2.5F;
            }
        });
    }
}