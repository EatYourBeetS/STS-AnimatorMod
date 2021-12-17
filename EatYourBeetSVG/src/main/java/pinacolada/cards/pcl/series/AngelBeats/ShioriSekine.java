package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;

import static pinacolada.resources.GR.Enums.CardTags.AFTERLIFE;

public class ShioriSekine extends PCLCard
{
    public static final PCLCardData DATA = Register(ShioriSekine.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ShioriSekine()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Light(1, 0, 0);
        SetEthereal(true);
        SetExhaust(true);
        SetAfterlife(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return CombatStats.CardsExhaustedThisTurn().size() > 0;
    }

    @Override
    public void triggerOnManualDiscard()
    {
        PCLActions.Bottom.GainEndurance(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Top.PlayFromPile(name, magicNumber, m, p.exhaustPile).SetOptions(true, false).SetFilter(c -> c.hasTag(AFTERLIFE)).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                PCLActions.Top.Purge(c);
            }
        });
    }
}