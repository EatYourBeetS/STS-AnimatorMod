package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.stances.SuperchargeStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MitsuKusabue extends AnimatorCard
{
    public static final EYBCardData DATA =
            Register(MitsuKusabue.class)
                    .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public MitsuKusabue() {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 2, 0, 0);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Draw(magicNumber)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards) {
                        GameUtilities.ModifyCostForTurn(card, 1, true);
                        GameUtilities.Retain(card);
                        if (SuperchargeStance.IsActive() && GameUtilities.HasLightAffinity(card)) {
                            GameActions.Bottom.GainTemporaryHP(secondaryValue);
                        }
                    }
                    GameActions.Last.Add(new RefreshHandLayout());
                });
    }
}
