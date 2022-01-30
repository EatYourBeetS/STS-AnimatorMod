package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.pcl.special.RinKaenbyou_VengefulSpirit;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class RinKaenbyou extends PCLCard
{
    public static final PCLCardData DATA = Register(RinKaenbyou.class)
            .SetSkill(0, CardRarity.RARE, PCLCardTarget.None)
            .SetSeriesFromClassPackage(true)
            .PostInitialize(data -> data.AddPreview(new RinKaenbyou_VengefulSpirit(), false));

    public RinKaenbyou()
    {
        super(DATA);

        Initialize(0, 0, 10, 12);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
        SetPurge(true);
    }

    @Override
    public int GetXValue() {
        return player.hand.size() + player.drawPile.size();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromPile(name, player.discardPile.size(), player.discardPile)
                .ShowEffect(true, true)
                .SetOptions(CardSelection.Top, true)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        PCLCard eCard = PCLJUtils.SafeCast(c, PCLCard.class);
                        if (eCard != null && eCard.affinities != null) {
                            PCLGameUtilities.AddAffinities(eCard.affinities, true);
                        }
                    }

                    for (int i = GetXValue(); i < magicNumber; i++) {
                        PCLActions.Bottom.MakeCardInDiscardPile(new RinKaenbyou_VengefulSpirit());
                    }
                });

        if (PCLGameUtilities.GetCurrentMatchCombo() >= secondaryValue && info.TryActivateLimited()) {
            PCLActions.Bottom.StackPower(new IntangiblePlayerPower(p, 1));
        }
    }
}

