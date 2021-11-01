package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken_Green;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.GameActions;

public class Nyanta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nyanta.class)
            .SetSkill(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Nyanta()
    {
        super(DATA);

        Initialize(0, 5, 2, 2);
        SetUpgrade(0, 2, 1, 1);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Orange(2, 0, 1);

        SetRetainOnce(true);

        SetAffinityRequirement(Affinity.Green, 3);
        SetAffinityRequirement(Affinity.Orange, 3);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainTemporaryThorns(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Orange,Affinity.Green).AddConditionalCallback((cards) -> {
            for (AbstractCard c : cards) {
                if (c.cardID.equals(AffinityToken_Green.DATA.ID)) {
                    GameActions.Bottom.GainWillpower(magicNumber);
                    GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
                }
                else {
                    GameActions.Bottom.GainAgility(magicNumber);
                    GameActions.Bottom.ChangeStance(WillpowerStance.STANCE_ID);
                }
            }
        });
    }
}