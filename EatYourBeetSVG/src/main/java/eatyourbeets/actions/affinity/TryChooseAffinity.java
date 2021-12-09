package eatyourbeets.actions.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AffinityChoice;
import eatyourbeets.cards.base.AffinityChoiceBuilder;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GridCardSelectScreenPatch;
import eatyourbeets.ui.animator.combat.EYBCardAffinitySystem;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.List;

public class TryChooseAffinity extends EYBActionWithCallback<ArrayList<AffinityChoice>>
{
    protected static final EYBCardAffinitySystem System = CombatStats.Affinities;
    protected final ArrayList<AffinityChoice> selectedCards = new ArrayList<>();
    protected final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    protected ArrayList<GenericCallback<ArrayList<AffinityChoice>>> conditionalCallbacks = new ArrayList<>();
    protected Affinity[] affinities;
    protected EYBCardAffinities sourceAffinities;
    protected ListSelection<AbstractCard> origin;
    protected int cost;
    protected boolean hideTopPanel;
    protected boolean canPlayerCancel;
    protected boolean anyNumber;
    protected boolean selected;

    public TryChooseAffinity(String sourceName, int cost)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, cost, Affinity.Extended());
    }

    public TryChooseAffinity(String sourceName, int cost, Affinity... affinities)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, cost, affinities);
    }

    public TryChooseAffinity(String sourceName, int amount, int cost)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, cost, Affinity.Extended());
    }

    public TryChooseAffinity(ActionType type, String sourceName, int amount, int cost, Affinity... affinities)
    {
        super(type);

        if (JUtils.Find(affinities, af -> af == Affinity.General) != null) {
            this.affinities = Affinity.Extended();
        }
        else {
            this.affinities = affinities;
        }
        this.canPlayerCancel = false;
        this.cost = cost;
        this.message = GR.Common.Strings.GridSelection.ChooseCards_F1;

        Initialize(amount, sourceName);
    }

    public TryChooseAffinity HideTopPanel(boolean hideTopPanel)
    {
        this.hideTopPanel = hideTopPanel;

        return this;
    }

    public TryChooseAffinity CancellableFromPlayer(boolean value)
    {
        this.canPlayerCancel = value;

        return this;
    }

    public TryChooseAffinity SetMessage(String message)
    {
        this.message = message;

        return this;
    }

    public TryChooseAffinity SetMessage(String format, Object... args)
    {
        this.message = JUtils.Format(format, args);

        return this;
    }

    public TryChooseAffinity SetOptions(boolean isRandom, boolean anyNumber)
    {
        return SetOptions(isRandom ? CardSelection.Random : null, anyNumber);
    }

    public TryChooseAffinity SetOptions(ListSelection<AbstractCard> origin, boolean anyNumber)
    {
        this.anyNumber = anyNumber;
        this.origin = origin;

        return this;
    }

    public TryChooseAffinity SetSourceAffinities(EYBCardAffinities sourceAffinities)
    {
        this.sourceAffinities = sourceAffinities;

        return this;
    }

    public <S> EYBActionWithCallback<ArrayList<AffinityChoice>> AddConditionalCallback(S state, ActionT2<S, ArrayList<AffinityChoice>> onCompletion)
    {
        conditionalCallbacks.add(GenericCallback.FromT2(onCompletion, state));

        return this;
    }

    public TryChooseAffinity AddConditionalCallback(ActionT1<ArrayList<AffinityChoice>> onCompletion)
    {
        conditionalCallbacks.add(GenericCallback.FromT1(onCompletion));

        return this;
    }

    public TryChooseAffinity AddConditionalCallback(ActionT0 onCompletion)
    {
        conditionalCallbacks.add(GenericCallback.FromT0(onCompletion));

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (hideTopPanel)
        {
            GameUtilities.SetTopPanelVisible(false);
        }

        for (Affinity affinity : affinities) {
            AffinityChoice aCard = GetCard(affinity);
            if (aCard != null) {
                group.addToTop(aCard);
            }
        }

        if (group.isEmpty()) {
            Complete();
            return;
        }

        if (origin != null)
        {
            List<AbstractCard> temp = new ArrayList<>(group.group);

            boolean remove = origin.mode.IsRandom();
            int max = Math.min(temp.size(), amount);
            for (int i = 0; i < max; i++)
            {
                final AffinityChoice card = (AffinityChoice) origin.Get(temp, i, remove);
                if (card != null)
                {
                    selectedCards.add(card);
                }
            }

            selected = true;
            GridCardSelectScreenPatch.Clear();
            Complete(selectedCards);
        }
        else
        {
            if (anyNumber)
            {
                AbstractDungeon.gridSelectScreen.open(group, amount, true, UpdateMessage());
            }
            else
            {
                if (canPlayerCancel)
                {
                    // Setting canCancel to true does not ensure the cancel button will be shown...
                    AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
                }
                else if (amount > 1 && amount > group.size())
                {
                    AbstractDungeon.gridSelectScreen.selectedCards.addAll(group.group);
                    return;
                }

                AbstractDungeon.gridSelectScreen.open(group, Math.min(group.size(), amount), UpdateMessage(), false, false, canPlayerCancel, false);
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
        {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (c instanceof AffinityChoice) {
                    selectedCards.add((AffinityChoice) c);
                }
            }
            selected = true;

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            GridCardSelectScreenPatch.Clear();
        }

        if (selected)
        {
            if (TickDuration(deltaTime))
            {
                for (AbstractCard card : selectedCards) {
                    card.use(player, null);
                }
                for (GenericCallback<ArrayList<AffinityChoice>> callback : conditionalCallbacks)
                {
                    callback.Complete(selectedCards);
                }
                Complete(selectedCards);
            }
            return;
        }

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) // cancelled
        {
            Complete();
        }
    }

    @Override
    protected void Complete()
    {
        if (hideTopPanel)
        {
            GameUtilities.SetTopPanelVisible(true);
        }

        super.Complete();
    }

    protected AffinityChoice GetCard(Affinity affinity) {
        AffinityToken token = AffinityToken.GetCard(affinity);
        AffinityChoiceBuilder builder = new AffinityChoiceBuilder(affinity, cost);
        return builder.Build();
    }
}
