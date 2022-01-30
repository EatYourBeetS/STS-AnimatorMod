package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericEffect_Obtain extends GenericEffect
{
    public static final String ID = Register(GenericEffect_Obtain.class);

    protected ArrayList<PCLCardData> cardData;

    public GenericEffect_Obtain(int copies, int upgradeTimes, PCLCardData... cards)
    {
        super(ID, JoinEntityIDs(cards, card -> card.ID), PCLCardTarget.Self, copies, upgradeTimes);
        this.cardData = new ArrayList<>(Arrays.asList(cards));
    }

    public GenericEffect_Obtain Add(PCLCardData newCard) {
        this.cardData.add(newCard);
        this.entityID = JoinEntityIDs(cardData, card -> card.ID);
        return this;
    }

    @Override
    public String GetText()
    {
        String joinedString = PCLJUtils.JoinStrings(" ", PCLJUtils.Map(cardData, card -> card.Strings.NAME));
        return GR.PCL.Strings.Actions.Obtain(joinedString, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        for (PCLCardData cd : cardData) {
            for (int i = 0; i < amount; i++) {
                AbstractCard c = cd.MakeCopy(false);
                for (int j = 0; j < misc; j++) {
                    c.upgrade();
                }
                PCLActions.Bottom.MakeCardInHand(c);
            }
        }

    }
}
