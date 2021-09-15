package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class LisaMinci extends AnimatorCard {
    public static final EYBCardData DATA = Register(LisaMinci.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public LisaMinci() {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(2, 0, 0);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Top.Scry(magicNumber)
                .AddCallback(cards -> {
                    for (AbstractCard card : cards)
                    {
                        boolean activateEffect1 = false;
                        boolean activateEffect2 = false;
                        boolean activateEffect3 = false;
                        switch (card.cost) {
                            case 0:
                            case -2:
                                activateEffect1 = true;
                                break;
                            case 1:
                            case -1:
                                activateEffect2 = true;
                                break;
                            default:
                                activateEffect3 = true;
                                break;
                        }

                        if (activateEffect1) {
                            GameActions.Bottom.GainIntellect(secondaryValue, upgraded);
                        }
                        if (activateEffect2) {
                            GameActions.Bottom.StackPower(new EnergizedPower(p, secondaryValue));
                        }
                        if (activateEffect3) {
                            GameActions.Bottom.ChannelOrb(new Lightning());
                        }
                    }
                });
    }
}