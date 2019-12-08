package eatyourbeets.actions._legacy.common;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class OnCardDrawnAction extends AbstractGameAction
{
    private final BiConsumer<Object, ArrayList<AbstractCard>> onDraw;
    private final ArrayList<AbstractCard> cardsDrawn;
    private final Object context;

    private boolean shuffleCheck;

    public OnCardDrawnAction(AbstractCreature source, int amount, BiConsumer<Object, ArrayList<AbstractCard>> onDraw, Object context)
    {
        this(source, amount, onDraw, context, false);
    }

    public OnCardDrawnAction(AbstractCreature source, int amount, BiConsumer<Object, ArrayList<AbstractCard>> onDraw, Object context, boolean endTurnDraw)
    {
        this.context = context;
        this.cardsDrawn = new ArrayList<>();
        this.onDraw = onDraw;
        this.shuffleCheck = false;
        if (endTurnDraw)
        {
            AbstractDungeon.topLevelEffects.add(new PlayerTurnEffect());
        }
        else if (AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID))
        {
            AbstractDungeon.player.getPower(NoDrawPower.POWER_ID).flash();
            this.setValues(AbstractDungeon.player, source, amount);
            this.isDone = true;
            this.duration = 0.0F;
            this.actionType = ActionType.WAIT;
            return;
        }

        this.setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.DRAW;
        if (Settings.FAST_MODE)
        {
            this.duration = Settings.ACTION_DUR_XFAST;
        }
        else
        {
            this.duration = Settings.ACTION_DUR_FASTER;
        }

    }

    public void update()
    {
        if (this.amount <= 0)
        {
            this.isDone = true;
        }
        else
        {
            int deckSize = AbstractDungeon.player.drawPile.size();
            int discardSize = AbstractDungeon.player.discardPile.size();
            if (!SoulGroup.isActive())
            {
                if (deckSize + discardSize == 0)
                {
                    this.isDone = true;
                }
                else if (AbstractDungeon.player.hand.size() == 10)
                {
                    AbstractDungeon.player.createHandIsFullDialog();
                    this.isDone = true;
                }
                else
                {
                    if (!this.shuffleCheck)
                    {
                        int tmp;
                        if (this.amount + AbstractDungeon.player.hand.size() > 10)
                        {
                            tmp = 10 - (this.amount + AbstractDungeon.player.hand.size());
                            this.amount += tmp;
                            AbstractDungeon.player.createHandIsFullDialog();
                        }

                        if (this.amount > deckSize)
                        {
                            tmp = this.amount - deckSize;
                            AbstractDungeon.actionManager.addToTop(new OnCardDrawnAction(AbstractDungeon.player, tmp, onDraw, context));// DrawCardAction(AbstractDungeon.player, allowed));
                            AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
                            if (deckSize != 0)
                            {
                                AbstractDungeon.actionManager.addToTop(new OnCardDrawnAction(AbstractDungeon.player, deckSize, onDraw, context));// DrawCardAction(AbstractDungeon.player, deckSize));
                            }

                            this.amount = 0;
                            this.isDone = true;
                        }

                        this.shuffleCheck = true;
                    }

                    this.duration -= Gdx.graphics.getDeltaTime();
                    if (this.amount != 0 && this.duration < 0.0F)
                    {
                        if (Settings.FAST_MODE)
                        {
                            this.duration = Settings.ACTION_DUR_XFAST;
                        }
                        else
                        {
                            this.duration = Settings.ACTION_DUR_FASTER;
                        }

                        --this.amount;
                        if (!AbstractDungeon.player.drawPile.isEmpty())
                        {
                            cardsDrawn.add(AbstractDungeon.player.drawPile.getTopCard());

                            AbstractDungeon.player.draw();
                            AbstractDungeon.player.hand.refreshHandLayout();
                        }
                        else
                        {
                            GameUtilities.GetLogger(getClass()).warn("Player attempted to draw from an empty draw pile mid-DrawAction?MASTER DECK: " + AbstractDungeon.player.masterDeck.getCardNames());
                            this.isDone = true;
                        }

                        if (this.amount == 0)
                        {
                            this.isDone = true;
                        }
                    }

                }
            }
        }

        if (this.isDone)
        {
            onDraw.accept(context, cardsDrawn);
        }
    }
}
