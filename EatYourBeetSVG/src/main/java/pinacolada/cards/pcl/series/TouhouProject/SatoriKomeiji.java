package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.MentalFortress;
import com.megacrit.cardcrawl.cards.purple.Nirvana;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MentalFortressPower;
import com.megacrit.cardcrawl.powers.watcher.NirvanaPower;
import com.megacrit.cardcrawl.relics.FrozenEye;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SatoriKomeiji extends PCLCard
{
    public static final PCLCardData DATA = Register(SatoriKomeiji.class)
            .SetPower(3, CardRarity.RARE)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new FakeAbstractCard(new Nirvana()), false);
            });
    public static final EYBCardPreview MENTAL_FORTRESS = new EYBCardPreview(new FakeAbstractCard(new MentalFortress()), false);

    public SatoriKomeiji()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetCostUpgrade(-1);

        SetAffinityRequirement(PCLAffinity.General, 10);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            this.cardText.OverrideDescription(form == 0 ? cardData.Strings.DESCRIPTION : null, true);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return auxiliaryData.form == 1 ? MENTAL_FORTRESS : super.GetCardPreview();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new SatoriPower(p, magicNumber));
        if (CheckAffinity(PCLAffinity.General) && info.TryActivateLimited()) {
            if (auxiliaryData.form == 1) {
                PCLActions.Bottom.StackPower(new MentalFortressPower(p, 4));
            }
            else {
                PCLActions.Bottom.StackPower(new NirvanaPower(p, 3));
            }
        }
    }

    public static class SatoriPower extends PCLPower
    {
        public SatoriPower(AbstractCreature owner, int amount)
        {
            super(owner, SatoriKomeiji.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.SetCombatData(FrozenEye.ID, true);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.GetCombatData(FrozenEye.ID, false);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            if (player.drawPile.size() == 1)
            {
                return;
            }

            PCLActions.Top.SelectFromPile(name, amount, player.drawPile)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    player.drawPile.removeCard(c);
                    player.drawPile.addToTop(c);
                }

                PCLGameUtilities.RefreshHandLayout();
            });

            if (player.drawPile.isEmpty() && player.discardPile.size() > 0)
            {
                PCLActions.Top.Add(new EmptyDeckShuffleAction());
            }
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }
}

