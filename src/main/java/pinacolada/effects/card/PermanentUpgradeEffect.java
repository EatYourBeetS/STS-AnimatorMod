package pinacolada.effects.card;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.ListSelection;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class PermanentUpgradeEffect extends EYBEffectWithCallback<AbstractCard>
{
    private GenericCondition<AbstractCard> filter;
    private ListSelection<AbstractCard> selection;
    private AbstractCard card;

    public PermanentUpgradeEffect()
    {
        super(0.2f, true);
    }

    public PermanentUpgradeEffect SetFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> PermanentUpgradeEffect SetFilter(S state, FuncT2<Boolean, S, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    public PermanentUpgradeEffect SetSelection(ListSelection<AbstractCard> selection)
    {
        this.selection = selection;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        final ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        for (AbstractCard c : player.masterDeck.group)
        {
            if (c.canUpgrade() && filter.Check(c))
            {
                upgradableCards.add(c);
            }
        }

        if (upgradableCards.size() > 0)
        {
            if (selection == null)
            {
                selection = CardSelection.Random(AbstractDungeon.miscRng);
            }

            card = selection.Get(upgradableCards, 1, false);
            card.upgrade();
            player.bottledCardUpgradeCheck(card);

            final float x = Settings.WIDTH * (0.4f + (0.1f * PCLJUtils.Count(AbstractDungeon.topLevelEffects, e -> e instanceof PermanentUpgradeEffect)));
            final float y = Settings.HEIGHT * 0.5f;

            PCLGameEffects.TopLevelQueue.ShowCardBriefly(card.makeStatEquivalentCopy(), x + AbstractCard.IMG_WIDTH * 0.5f + 20f * Settings.scale, y);
            PCLGameEffects.TopLevelQueue.Add(new UpgradeShineEffect(x, y));
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(card);
        }
    }

    @Override
    public void render(SpriteBatch sb) { }

    @Override
    public void dispose() { }
}
