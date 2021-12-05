package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class LisaMinci extends AnimatorCard {
    public static final EYBCardData DATA = Register(LisaMinci.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public LisaMinci() {
        super(DATA);

        Initialize(0, 1, 3, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(2, 0, 2);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(Affinity.Blue, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Scry(magicNumber)
                .AddCallback(cards -> {
                    boolean activateEffect1 = false;
                    boolean activateEffect2 = false;
                    boolean activateEffect3 = false;
                    for (AbstractCard card : cards)
                    {
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
                    }
                    if (activateEffect1) {
                        GameActions.Bottom.GainFocus(secondaryValue, true);
                    }
                    if (activateEffect2) {
                        GameActions.Bottom.DrawNextTurn(secondaryValue);
                    }
                    if (activateEffect3) {
                        GameActions.Bottom.ChannelOrb(new Lightning());
                    }
                });
    }
}