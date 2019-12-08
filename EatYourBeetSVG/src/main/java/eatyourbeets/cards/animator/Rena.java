package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Rena extends AnimatorCard
{
    public static final String ID = Register(Rena.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Discard);

    public Rena()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 3, 0, 2);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        AbstractPlayer p =  AbstractDungeon.player;
        GameActionsHelper.ApplyPower(p, p, new BlurPower(p, this.secondaryValue), this.secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.GainBlock(this.block);
        GameActionsHelper.MakeCardInHand(ThrowingKnife.GetRandomCard(), 1, false);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new BlurPower(p, this.secondaryValue), this.secondaryValue);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}