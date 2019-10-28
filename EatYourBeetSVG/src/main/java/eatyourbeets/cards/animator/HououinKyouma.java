package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import patches.AbstractEnums;

public class HououinKyouma extends AnimatorCard
{
    public static final String ID = Register(HououinKyouma.class.getSimpleName());

    public HououinKyouma()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0);

        this.tags.add(AbstractEnums.CardTags.PURGE);

        AddExtendedDescription();

        SetSynergy(Synergies.SteinsGate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat)
        {
            if (!c.cardID.equals(this.cardID))
            {
                boolean canAdd = true;
                for (AbstractCard c2 : group.group)
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
                    if (upgraded)
                    {
                        copy.setCostForTurn(copy.costForTurn - 1);
                    }

                    group.addToTop(copy.makeStatEquivalentCopy());
                }
            }
        }

        if (group.size() > 0)
        {
            GameActionsHelper.AddToBottom(new FetchAction(group, 1));
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}