package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cecily extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Cecily.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int LIMIT = 4;

    public Cecily()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0,0,1);

        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public int GetXValue() {
        return Math.min(LIMIT,CombatStats.Affinities.GetAffinityLevel(Affinity.General, true));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new CecilyPower(p, secondaryValue));
        GameActions.Bottom.TryChooseSpendAffinity(name, GetXValue()).AddConditionalCallback((cards) -> {
            if (cards.size() > 0) {
                GameActions.Bottom.Cycle(name, cards.get(0).magicNumber).AddCallback(() -> {
                    if (info.IsSynergizing && info.TryActivateLimited()) {
                        GameActions.Bottom.Motivate(secondaryValue);
                    }
                });
            }

        });
    }

    public static class CecilyPower extends AnimatorPower
    {
        public CecilyPower(AbstractCreature owner, int amount)
        {
            super(owner, Cecily.DATA);

            Initialize(amount);
        }

        @Override
        public void onCardDraw(AbstractCard c)
        {
            super.onCardDraw(c);
            GameActions.Last.ModifyAffinityLevel(c, Affinity.General, 2, false)
                    .SetFilter(GameUtilities::HasUpgradableAffinities);
            ReducePower(1);
        }
    }
}