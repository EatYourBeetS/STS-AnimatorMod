package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import patches.AbstractEnums;

public class HououinKyouma extends AnimatorCard
{
    public static final String ID = Register(HououinKyouma.class.getSimpleName());

    public HououinKyouma()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0);

        this.tags.add(AbstractEnums.CardTags.PURGE);

        AddExtendedDescription();

        SetSynergy(Synergies.SteinsGate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat)
        {
            if (!c.cardID.equals(this.cardID))
            {
                boolean canAdd = true;
                for (AbstractCard c2 : choices.group)
                {
                    if (c.cardID.equals(c2.cardID) && c.timesUpgraded == c2.timesUpgraded)
                    {
                        canAdd = false;
                        break;
                    }
                }

                if (canAdd)
                {
                    AbstractCard copy = c.makeStatEquivalentCopy();
                    copy.retain = true;
                    choices.addToTop(copy);
                }
            }
        }

        if (choices.size() > 0)
        {
            GameActions.Bottom.SelectFromPile(name, 1, choices)
            .SetOptions(false, false)
            .SetMessage(CardRewardScreen.TEXT[1])
            .AddCallback(cards -> GameActions.Bottom.MakeCardInHand(cards.get(0)));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }
}