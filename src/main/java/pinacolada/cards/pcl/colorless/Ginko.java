package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.curse.Curse_SearingBurn;
import pinacolada.cards.pcl.status.*;
import pinacolada.utilities.PCLActions;

public class Ginko extends PCLCard
{
    public static final PCLCardData DATA = Register(Ginko.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Mushishi);

    public Ginko()
    {
        super(DATA);

        Initialize(0, 3, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 0, 1);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.SelectFromPile(name, magicNumber, player.hand)
                .SetOptions(false, true)
                .SetFilter(c -> c.type == CardType.STATUS)
                .AddCallback(cards -> {
                    if (cards.size() > 0) {
                        for (AbstractCard c : cards) {
                            TransformCard(c);
                        }
                    }
                });
    }

    private void TransformCard(AbstractCard c) {
        if (c instanceof Burn || c instanceof Status_Burn) {
            PCLActions.Last.ReplaceCard(c.uuid, new Curse_SearingBurn());
        }
        else if (c instanceof Status_Frostbite || c instanceof Status_Slimed || c instanceof Status_Wound || c instanceof Status_Void || c instanceof Status_Dazed || c instanceof Curse_SearingBurn) {
            ((PCLCard) c).SetForm(1,c.timesUpgraded);
        }
        else {
            PCLActions.Last.ReplaceCard(c.uuid, new Crystallize());
        }
    }
}