package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.TimeParadox;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HououinKyouma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HououinKyouma.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetMaxCopies(1)
            .SetMultiformData(2)
            .SetSeries(CardSeries.SteinsGate)
            .PostInitialize(data ->
                    data.AddPreview(new TimeParadox(), false)
            );

    public HououinKyouma()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);

        SetAffinity_Blue(1);
        SetAffinity_Silver(2);

        SetRetainOnce(true);
        SetPurge(true);
        SetDelayed(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void triggerWhenDrawn()
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainTechnic(secondaryValue);
            GameActions.Bottom.MakeCardInDiscardPile(new TimeParadox());
            GameUtilities.Retain(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
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
            GameActions.Bottom.SelectFromPile(name, magicNumber, choices)
            .SetOptions(false, false)
            .AddCallback(cards -> GameActions.Bottom.MakeCardInDrawPile(cards.get(0)).AddCallback(GameUtilities::Retain));
        }
    }
}