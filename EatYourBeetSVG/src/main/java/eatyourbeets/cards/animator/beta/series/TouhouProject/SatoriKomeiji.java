package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.NirvanaPower;
import com.megacrit.cardcrawl.relics.FrozenEye;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SatoriKomeiji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SatoriKomeiji.class).SetPower(3, CardRarity.RARE).SetSeriesFromClassPackage();

    public SatoriKomeiji()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetCostUpgrade(-1);

        SetAffinityRequirement(Affinity.General, 10);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new SatoriPower(p, magicNumber));
        if (CheckAffinity(Affinity.General) && info.TryActivateLimited()) {
            GameActions.Bottom.StackPower(new NirvanaPower(p, secondaryValue));
        }
    }

    public static class SatoriPower extends AnimatorPower
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

            GameActions.Top.SelectFromPile(name, amount, player.drawPile)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    player.drawPile.removeCard(c);
                    player.drawPile.addToTop(c);
                }

                GameUtilities.RefreshHandLayout();
            });

            if (player.drawPile.isEmpty() && player.discardPile.size() > 0)
            {
                GameActions.Top.Add(new EmptyDeckShuffleAction());
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

