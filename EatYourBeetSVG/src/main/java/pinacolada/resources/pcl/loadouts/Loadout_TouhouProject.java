package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Clumsy;
import pinacolada.cards.pcl.curse.Curse_Regret;
import pinacolada.cards.pcl.series.TouhouProject.*;
import pinacolada.cards.pcl.ultrarare.YuyukoSaigyouji;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_TouhouProject extends PCLLoadout
{
    public Loadout_TouhouProject()
    {
        super(CardSeries.TouhouProject);
        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(MarisaKirisame.DATA, 4);
        AddStarterCard(ReimuHakurei.DATA, 5);
        AddStarterCard(MayumiJoutouguu.DATA, 5);
        AddStarterCard(YuukaKazami.DATA, 7);
        AddStarterCard(SuikaIbuki.DATA, 10);
        AddStarterCard(Cirno.DATA, 11);
        AddStarterCard(FlandreScarlet.DATA, 15);
        AddStarterCard(KaguyaHouraisan.DATA, 20);
        AddStarterCard(YukariYakumo.DATA, 26);
        AddStarterCard(Curse_Regret.DATA, -8);
        AddStarterCard(Curse_Clumsy.DATA, -3);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return AyaShameimaru.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return YuyukoSaigyouji.DATA;
    }
}
