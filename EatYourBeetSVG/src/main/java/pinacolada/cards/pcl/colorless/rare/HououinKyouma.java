package pinacolada.cards.pcl.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.TimeParadox;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HououinKyouma extends PCLCard
{
    public static final PCLCardData DATA = Register(HououinKyouma.class)
            .SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
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
        SetAffinity_Silver(1);
        SetCostUpgrade(-1);

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
            PCLActions.Bottom.GainTechnic(secondaryValue);
            PCLActions.Bottom.MakeCardInDrawPile(new TimeParadox());
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
            PCLActions.Bottom.SelectFromPile(name, magicNumber, choices)
            .SetOptions(false, false)
            .AddCallback(cards -> PCLActions.Bottom.MakeCardInDrawPile(cards.get(0)).AddCallback(PCLGameUtilities::Retain));
        }
    }
}