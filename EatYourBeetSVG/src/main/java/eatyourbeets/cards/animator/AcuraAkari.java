package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.TemporaryEnvenomPower;

public class AcuraAkari extends AnimatorCard
{
    public static final String ID = CreateFullID(AcuraAkari.class.getSimpleName());

    public AcuraAkari()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        this.baseSecondaryValue = this.secondaryValue = 2;

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        CardGroup hand = AbstractDungeon.player.hand;
        int toDiscard = 0;
        if (hand.contains(this))
        {
            toDiscard = -1;
        }
        toDiscard += hand.size();

        return toDiscard >= 1 && super.cardPlayable(m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.Discard(1, false);

        for (int i = 0; i < secondaryValue; i++)
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(ThrowingKnife.GetRandomCard()));
        }

        GameActionsHelper.ApplyPower(p, p, new TemporaryEnvenomPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}