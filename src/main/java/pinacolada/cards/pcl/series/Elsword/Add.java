package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.OrbCore;
import pinacolada.cards.pcl.status.Crystallize;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class Add extends PCLCard
{
    public static final PCLCardData DATA = Register(Add.class)
            .SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Crystallize(), false);
                data.AddPreviews(OrbCore.GetAllCores(), false);
            });

    public Add()
    {
        super(DATA);

        Initialize(0, 6, 2, 3);
        SetUpgrade(0, 2, 1, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Silver(1,0,2);

        SetAffinityRequirement(PCLAffinity.Dark, 7);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainEnergyNextTurn(secondaryValue);
        PCLActions.Bottom.DrawNextTurn(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.MakeCardInDrawPile(new Crystallize()).Repeat(secondaryValue).AddCallback(() -> {
            if (CheckAffinity(PCLAffinity.Dark) && info.TryActivateLimited() && TrySpendAffinity(PCLAffinity.Dark))
            {
                PCLActions.Bottom.ExhaustFromPile(name, 1, p.hand, p.drawPile, p.discardPile)
                        .AddCallback(this::OnCardChosen);
            }
        });


    }

    private void OnCardChosen(ArrayList<AbstractCard> cards)
    {
        if (cards != null && cards.size() > 0)
        {
            CardGroup cardGroup = null;
            final AbstractCard c = cards.get(0);
            if (player.hand.contains(c))
            {
                cardGroup = player.hand;
            }
            else if (player.drawPile.contains(c))
            {
                cardGroup = player.drawPile;
            }
            else if (player.discardPile.contains(c))
            {
                cardGroup = player.discardPile;
            }

            if (cardGroup != null)
            {
                PCLActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                .AddCallback(cardGroup, this::OrbChosen));
            }
        }
    }

    private void OrbChosen(CardGroup cardGroup, ArrayList<AbstractCard> chosen)
    {
        if (cardGroup != null && chosen != null && chosen.size() == 1)
        {
            switch (cardGroup.type)
            {
                case DRAW_PILE:
                case HAND:
                case DISCARD_PILE:
                    PCLActions.Bottom.MakeCard(chosen.get(0), cardGroup);
                    break;

                default:
                    PCLJUtils.LogWarning(this, "Invalid card group type: " + cardGroup.type);
                    break;
            }
        }
    }
}