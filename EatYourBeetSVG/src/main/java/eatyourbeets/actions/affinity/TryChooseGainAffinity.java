package eatyourbeets.actions.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCardBuilder;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainAffinity;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GridCardSelectScreenPatch;
import eatyourbeets.ui.animator.combat.EYBCardAffinitySystem;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.List;

public class TryChooseGainAffinity extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected static final EYBCardAffinitySystem System = CombatStats.Affinities;
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    protected ArrayList<GenericCallback<ArrayList<AbstractCard>>> conditionalCallbacks = new ArrayList<>();
    protected Affinity[] affinities;
    protected ListSelection<AbstractCard> origin;
    protected int gain;
    protected boolean hideTopPanel;
    protected boolean canPlayerCancel;
    protected boolean anyNumber;
    protected boolean selected;

    public TryChooseGainAffinity(String sourceName, int gain)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, gain, Affinity.Extended());
    }

    public TryChooseGainAffinity(String sourceName, int gain, Affinity... affinities)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, gain, affinities);
    }

    public TryChooseGainAffinity(String sourceName, int amount, int gain)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, gain, Affinity.Extended());
    }

    public TryChooseGainAffinity(ActionType type, String sourceName, int amount, int gain, Affinity... affinities)
    {
        super(type);

        // General affinity means that you can select any affinity
        if (JUtils.Find(affinities, af -> af == Affinity.General) != null) {
            this.affinities = Affinity.Extended();
        }
        else {
            this.affinities = affinities;
        }
        this.canPlayerCancel = false;
        this.gain = gain;
        this.message = GR.Common.Strings.GridSelection.ChooseCards_F1;

        Initialize(amount, sourceName);
    }

    public TryChooseGainAffinity HideTopPanel(boolean hideTopPanel)
    {
        this.hideTopPanel = hideTopPanel;

        return this;
    }

    public TryChooseGainAffinity CancellableFromPlayer(boolean value)
    {
        this.canPlayerCancel = value;

        return this;
    }

    public TryChooseGainAffinity SetMessage(String message)
    {
        this.message = message;

        return this;
    }

    public TryChooseGainAffinity SetMessage(String format, Object... args)
    {
        this.message = JUtils.Format(format, args);

        return this;
    }

    public TryChooseGainAffinity SetOptions(boolean isRandom, boolean anyNumber)
    {
        return SetOptions(isRandom ? CardSelection.Random : null, anyNumber);
    }

    public TryChooseGainAffinity SetOptions(ListSelection<AbstractCard> origin, boolean anyNumber)
    {
        this.anyNumber = anyNumber;
        this.origin = origin;

        return this;
    }

    public <S> EYBActionWithCallback<ArrayList<AbstractCard>> AddConditionalCallback(S state, ActionT2<S, ArrayList<AbstractCard>> onCompletion)
    {
        conditionalCallbacks.add(GenericCallback.FromT2(onCompletion, state));

        return this;
    }

    public TryChooseGainAffinity AddConditionalCallback(ActionT1<ArrayList<AbstractCard>> onCompletion)
    {
        conditionalCallbacks.add(GenericCallback.FromT1(onCompletion));

        return this;
    }

    public TryChooseGainAffinity AddConditionalCallback(ActionT0 onCompletion)
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
            GenericEffect_GainAffinity affinityGain = new GenericEffect_GainAffinity(affinity, gain);
            AffinityToken token = AffinityToken.GetCard(affinity);
            AnimatorCardBuilder builder = new AnimatorCardBuilder(token, affinityGain.GetText(), false).SetOnUse(affinityGain::Use).SetID(token.cardID).SetNumbers(0,0,gain,0, 1);
            group.addToTop(builder.Build());
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
                final AbstractCard card = origin.Get(temp, i, remove);
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
            selectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
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
                for (GenericCallback<ArrayList<AbstractCard>> callback : conditionalCallbacks)
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
}
