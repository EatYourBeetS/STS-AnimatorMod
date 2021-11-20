package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.animator.basic.ImprovedDefend;
import eatyourbeets.cards.animator.basic.ImprovedStrike;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.InputManager;

public class PolychromePaintbrush extends AnimatorRelic
{
    public static final String ID = CreateFullID(PolychromePaintbrush.class);
    public static final int MAX_STORED_USES = 3;
    public static final int USES_PER_ELITE = 1;

    public PolychromePaintbrush()
    {
        super(ID, RelicTier.STARTER, LandingSound.SOLID);
        SetCounter(0);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, USES_PER_ELITE, MAX_STORED_USES);
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        super.onEnterRoom(room);

        if (room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
        {
            SetCounter(Math.min(MAX_STORED_USES, counter + USES_PER_ELITE));
            flash();
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (hb.hovered && !AbstractDungeon.isScreenUp && InputManager.RightClick.IsJustPressed())
        {
            stopPulse();
            Use();
        }
    }

    public void Use()
    {
        if (counter > 0)
        {
            flash();
            GameEffects.Queue.Callback(
                    new SelectFromPile(name, 1, player.masterDeck)
                    .SetFilter(c -> c.rarity == AbstractCard.CardRarity.BASIC)
                    .HideTopPanel(true)
                    .AddCallback(selection -> {
                        for (AbstractCard c : selection) {
                            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                            for (EYBCardData dat : c.type == AbstractCard.CardType.ATTACK ? ImprovedStrike.GetCards() : ImprovedDefend.GetCards()) {
                                group.addToTop(dat.MakeCopy(c.upgraded));
                            }

                            GameEffects.Queue.Callback(new SelectFromPile(name, 1, group).HideTopPanel(true).AddCallback(newSelection -> {
                                for (AbstractCard c2 : newSelection) {
                                    player.masterDeck.group.remove(c);
                                    GameEffects.TopLevelQueue.ShowAndObtain(c2, Settings.WIDTH / 2f, Settings.HEIGHT / 2f, false);
                                }
                            }));
                        }
                    }));
            AddCounter(-1);
        }
    }

}