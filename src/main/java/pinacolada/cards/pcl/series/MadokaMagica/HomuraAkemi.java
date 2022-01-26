package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.HomuraAkemi_Homulily;
import pinacolada.utilities.PCLActions;

public class HomuraAkemi extends PCLCard
{
    public static final PCLCardData DATA = Register(HomuraAkemi.class)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new HomuraAkemi_Homulily(), true);
            });

    public HomuraAkemi()
    {
        super(DATA);

        Initialize(0, 3, 2, 3);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Light(0,0,1);
        SetAffinity_Silver(1, 0, 0);

        SetDelayed(true);
        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Light, 8);
        SetSoul(2, 0, HomuraAkemi_Homulily::new);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup[] choices = upgraded ? new CardGroup[] {player.hand,player.drawPile,player.discardPile,player.exhaustPile} : new CardGroup[] {player.hand,player.drawPile,player.discardPile};
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.PurgeFromPile(name,1,choices).AddCallback(
                pc -> {
                    if (pc.size() > 0) {
                        PCLActions.Bottom.FetchFromPile(name, magicNumber, player.exhaustPile)
                                .SetFilter( c -> CombatStats.CardsExhaustedThisTurn().contains(c));
                    }
                });

        if (TrySpendAffinity(PCLAffinity.Light) && info.TryActivateLimited()) {
            PCLActions.Bottom.GainArtifact(secondaryValue);
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

}
