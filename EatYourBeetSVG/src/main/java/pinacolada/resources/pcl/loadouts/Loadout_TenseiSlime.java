package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.TenseiSlime.*;
import pinacolada.cards.pcl.ultrarare.Veldora;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_TenseiSlime extends PCLLoadout
{
    public Loadout_TenseiSlime()
    {
        super(CardSeries.TenseiSlime);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Benimaru.DATA, 5);
        AddStarterCard(Shuna.DATA, 5);
        AddStarterCard(Gobta.DATA, 5);
        AddStarterCard(Gabiru.DATA, 6);
        AddStarterCard(Shion.DATA, 6);
        AddStarterCard(Hakurou.DATA, 7);
        AddStarterCard(Souei.DATA, 11);
        AddStarterCard(Ranga.DATA, 11);
        AddStarterCard(Millim.DATA, 14);
        AddStarterCard(Ramiris.DATA, 16);
        AddStarterCard(Rimuru.DATA, 17);
        AddStarterCard(Shizu.DATA, 21);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Rimuru.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Veldora.DATA;
    }
}
