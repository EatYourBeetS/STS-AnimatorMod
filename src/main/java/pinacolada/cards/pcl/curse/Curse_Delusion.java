package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.cards.pcl.tokens.AffinityToken_Green;
import pinacolada.utilities.PCLActions;

public class Curse_Delusion extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Delusion.class)
            .SetCurse(-2, eatyourbeets.cards.base.EYBCardTarget.None, false).SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(PCLAffinity.Green), true));

    public Curse_Delusion()
    {
        super(DATA, true);
        SetAffinity_Dark(1);
        SetUnplayable(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard) {
            PCLActions.Bottom.ModifyTag(player.drawPile, 1, AUTOPLAY, true);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();


        AffinityToken_Green token = new AffinityToken_Green();
        token.upgrade();
        PCLActions.Bottom.MakeCardInHand(token);
    }

}