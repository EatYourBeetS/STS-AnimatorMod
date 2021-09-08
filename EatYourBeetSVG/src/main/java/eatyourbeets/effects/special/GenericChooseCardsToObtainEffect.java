package eatyourbeets.effects.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class GenericChooseCardsToObtainEffect extends EYBEffectWithCallback<GenericChooseCardsToObtainEffect>
{
    private static final int GROUP_SIZE = 3;
    private final CardGroup[] groups;
    private final GenericCondition<AbstractCard> filter;
    private final RandomizedList<AbstractCard> offeredCards = new RandomizedList<>();
    private final Color screenColor;
    private int cardsToAdd;
    public final ArrayList<AbstractCard> cards = new ArrayList<>();

    public GenericChooseCardsToObtainEffect(int obtain) {
        this(obtain, null, AbstractDungeon.commonCardPool, AbstractDungeon.uncommonCardPool, AbstractDungeon.rareCardPool);
    }

    public GenericChooseCardsToObtainEffect(int obtain, FuncT1<Boolean, AbstractCard> filter) {
        this(obtain, filter, AbstractDungeon.commonCardPool, AbstractDungeon.uncommonCardPool, AbstractDungeon.rareCardPool);
    }

    public GenericChooseCardsToObtainEffect(int obtain, FuncT1<Boolean, AbstractCard> filter, CardGroup... groups)
    {
        super(0.75f, true);

        this.cardsToAdd = obtain;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.screenColor.a = 0f;
        this.groups = groups;
        this.filter = GenericCondition.FromT1(filter);
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        if (cardsToAdd > 0)
        {
            OpenPanel_Add();
        }
        else
        {
            Complete();
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (cardsToAdd > 0)
        {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() == cardsToAdd)
            {
                float displayCount = 0f;
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
                {
                    cards.add(card.makeCopy());
                    GameEffects.Queue.ShowAndObtain(card.makeCopy(), (float) Settings.WIDTH / 3f + displayCount, (float) Settings.HEIGHT / 2f, false);
                    displayCount += (float) Settings.WIDTH / 6f;
                }
                cardsToAdd = 0;
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.gridSelectScreen.targetGroup.clear();
            }
        }
        else if (TickDuration(deltaTime))
        {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            Complete(this);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        if (AbstractDungeon.screen == CurrentScreen.GRID)
        {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
    }

    public void OpenPanel_Add()
    {
        final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (offeredCards.Size() == 0)
        {
            for (CardGroup cGroup: groups) {
                for (AbstractCard card : cGroup.group) {
                    if (filter == null || filter.Check(card)) {
                        cardGroup.addToBottom(card);
                    }
                }
            }
            if (offeredCards.Size() < cardsToAdd)
            {
                JUtils.LogWarning(this, "Not enough cards");
                Complete(this);
                return;
            }
        }

        for (int i = 0; i < GROUP_SIZE; i++)
        {
            cardGroup.group.add(offeredCards.Retrieve(AbstractDungeon.cardRandomRng, true).makeCopy());
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(cardGroup, cardsToAdd, GR.Common.Strings.GridSelection.ChooseCards(cardsToAdd), false, false, false, false);
    }
}
