package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;

public class LisaMinci extends PCLCard {
    public static final PCLCardData DATA = Register(LisaMinci.class).SetSkill(2, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.Self).SetSeriesFromClassPackage();

    public LisaMinci() {
        super(DATA);

        Initialize(0, 1, 3, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(1, 0, 2);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(PCLAffinity.Blue, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.Scry(magicNumber)
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
                        PCLActions.Bottom.GainFocus(secondaryValue, true);
                    }
                    if (activateEffect2) {
                        PCLActions.Bottom.DrawNextTurn(secondaryValue);
                    }
                    if (activateEffect3) {
                        PCLActions.Bottom.ChannelOrb(new Lightning());
                    }
                });
    }
}