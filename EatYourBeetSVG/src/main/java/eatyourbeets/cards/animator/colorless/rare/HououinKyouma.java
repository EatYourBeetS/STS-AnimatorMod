package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HououinKyouma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HououinKyouma.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public HououinKyouma()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetPurge(true);
        SetSynergy(Synergies.SteinsGate);

        SetAffinity_Blue(1, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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
                    choices.addToTop(c.makeStatEquivalentCopy());
                }
            }
        }

        if (choices.size() > 0)
        {
            GameActions.Bottom.SelectFromPile(name, 1, choices)
            .SetOptions(false, false)
            .AddCallback(cards -> GameActions.Bottom.MakeCardInHand(cards.get(0)).AddCallback(GameUtilities::Retain));
        }
    }
}