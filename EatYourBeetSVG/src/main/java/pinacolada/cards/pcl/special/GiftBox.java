package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.relics.PCLEnchantableRelic;
import pinacolada.relics.pcl.AbstractMissingPiece;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class GiftBox extends PCLCard
{
    public static final PCLCardData DATA = Register(GiftBox.class)
            .SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public GiftBox()
    {
        super(DATA);

        Initialize(0, 0, 5, 12);
        SetUpgrade(0, 0, 2, 4);
        SetAffinity_Star(1, 0, 0);

        SetPurge(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        final ArrayList<AbstractCard> cards = PCLGameUtilities.GetAvailableCards();
        for (int i = 0; i < secondaryValue; i++)
        {
            PCLActions.Bottom.MakeCardInDrawPile(PCLGameUtilities.GetRandomElement(cards))
            .SetUpgrade(false, true)
            .SetDuration(0.01f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        //TODO: Make this into an actual card and not something for testing
        UnlockTracker.addScore(GR.Enums.Characters.THE_FOOL, 9999);
        AbstractDungeon.isAscensionMode = true;
        AbstractDungeon.ascensionLevel = 18;

        for (PCLAffinity af : PCLAffinity.Extended()) {
            PCLActions.Bottom.AddAffinity(af, 99);
            PCLActions.Bottom.StackAffinityPower(af, 99);
        }

        PCLEnchantableRelic enchantable = PCLGameUtilities.GetRelic(PCLEnchantableRelic.class);
        if (enchantable != null) {
            enchantable.AddCounter(99);
        }
        AbstractMissingPiece mp = PCLGameUtilities.GetRelic(AbstractMissingPiece.class);
        if (mp != null) {
            mp.AddCounter(99);
        }

    }
}