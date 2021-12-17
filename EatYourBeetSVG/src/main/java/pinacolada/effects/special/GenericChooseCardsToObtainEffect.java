package pinacolada.effects.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.effects.PCLEffectWithCallback;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class GenericChooseCardsToObtainEffect extends PCLEffectWithCallback<GenericChooseCardsToObtainEffect>
{
    private final CardGroup[] groups;
    private final GenericCondition<AbstractCard> filter;
    private final RandomizedList<AbstractCard> offeredCards = new RandomizedList<>();
    private final Color screenColor;
    private final int groupSize;
    private int cardsToAdd;
    public final ArrayList<AbstractCard> cards = new ArrayList<>();

    public GenericChooseCardsToObtainEffect(int obtain, int groupSize) {
        this(obtain, groupSize, null, AbstractDungeon.commonCardPool, AbstractDungeon.uncommonCardPool, AbstractDungeon.rareCardPool);
    }

    public GenericChooseCardsToObtainEffect(int obtain, int groupSize, FuncT1<Boolean, AbstractCard> filter) {
        this(obtain, groupSize, filter, AbstractDungeon.commonCardPool, AbstractDungeon.uncommonCardPool, AbstractDungeon.rareCardPool);
    }

    public GenericChooseCardsToObtainEffect(int obtain, int groupSize, FuncT1<Boolean, AbstractCard> filter, CardGroup... groups)
    {
        super(0.75f, true);

        this.cardsToAdd = obtain;
        this.groupSize = groupSize;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.screenColor.a = 0f;
        this.groups = groups;
        this.filter = filter != null ? GenericCondition.FromT1(filter) : null;
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
                    PCLGameEffects.Queue.ShowAndObtain(card.makeCopy(), (float) Settings.WIDTH / 3f + displayCount, (float) Settings.HEIGHT / 2f, false);
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
                        offeredCards.Add(card);
                    }
                }
            }
            if (offeredCards.Size() < cardsToAdd)
            {
                PCLJUtils.LogWarning(this, "Not enough cards");
                Complete(this);
                return;
            }
        }

        for (int i = 0; i < groupSize; i++)
        {
            cardGroup.group.add(offeredCards.Retrieve(AbstractDungeon.cardRandomRng, true).makeCopy());
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(cardGroup, cardsToAdd, GR.PCL.Strings.GridSelection.ChooseCards(cardsToAdd), false, false, false, false);
    }
}
