package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.utilities.PCLActions;

public class Gluttony extends PCLCard
{
    public static final PCLCardData DATA = Register(Gluttony.class)
            .SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(PCLAffinity.Dark), true));

    public Gluttony()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        int xValue = GetXValue();
        return xValue > 0 ? TempHPAttribute.Instance.SetCard(this).SetText(GetXValue(), Settings.CREAM_COLOR) : null;
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public int GetXValue() {
        return player != null ? (magicNumber * Math.min(player.drawPile.size(), secondaryValue)) : -1;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromPile(name, secondaryValue, player.drawPile)
                .ShowEffect(true, true)
                .SetOptions(CardSelection.Top, false)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        PCLActions.Bottom.GainDesecration(1);
                        PCLActions.Bottom.GainTemporaryHP(magicNumber);

                        if (c.type == CardType.CURSE && info.TryActivateLimited()) {
                            PCLActions.Bottom.ObtainAffinityToken(PCLAffinity.Dark, upgraded);
                        }
                    }
                });
    }
}