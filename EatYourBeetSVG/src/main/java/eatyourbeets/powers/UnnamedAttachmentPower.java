package eatyourbeets.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.powers.EYBFlashPowerEffect;
import eatyourbeets.effects.powers.EYBGainPowerEffect;
import eatyourbeets.powers.unnamed.AttachmentsPower;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

public abstract class UnnamedAttachmentPower extends UnnamedPower implements InvisiblePower
{
    public final UnnamedCard originalCard;
    public final ArrayList<UnnamedCard> cards = new ArrayList<>();

    public UnnamedAttachmentPower(AbstractCreature owner, UnnamedCard card)
    {
        this(owner, null, card);
    }

    public UnnamedAttachmentPower(AbstractCreature owner, AbstractCreature source, UnnamedCard card)
    {
        super(owner, card.cardData);

        this.originalCard = card;
        this.cards.add(card);
        this.source = source;
    }

    @Override
    public void updateDescription()
    {
        this.description = "";
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Top.StackPower(owner, new AttachmentsPower(owner, 1));
    }

    @Override
    protected void OnSamePowerApplied(AbstractPower power)
    {
        super.OnSamePowerApplied(power);

        final UnnamedAttachmentPower other = (UnnamedAttachmentPower) power;
        cards.addAll(other.cards);
        GameActions.Top.StackPower(owner, new AttachmentsPower(owner, other.cards.size()));
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        for (AbstractCard c : cards)
        {
            GameActions.Bottom.MoveCard(c, player.exhaustPile, player.discardPile)
            .SetDestination(CardSelection.Bottom)
            .ShowEffect(true, true);
        }
    }

    @Override
    public void flash()
    {
        this.effects.add(new EYBGainPowerEffect(this, true));
        GameEffects.Queue.Add(new EYBFlashPowerEffect(GetMainPower()));
    }

    @Override
    public void flashWithoutSound()
    {
        this.effects.add(new EYBGainPowerEffect(this, false));
        GameEffects.Queue.Add(new EYBFlashPowerEffect(GetMainPower()));
    }

    @Override
    public final AbstractPower makeCopy()
    {
        final UnnamedAttachmentPower copy = MakeCopy();
        copy.cards.clear();
        for (int i = 0; i < cards.size(); i++)
        {
            copy.cards.add((UnnamedCard)cards.get(i).makeStatEquivalentCopy());
        }

        return copy;
    }

    protected abstract UnnamedAttachmentPower MakeCopy();

    protected AttachmentsPower GetMainPower()
    {
        final AbstractPower power = owner.getPower(AttachmentsPower.POWER_ID);
        return power != null ? (AttachmentsPower) power : null;
    }
}
