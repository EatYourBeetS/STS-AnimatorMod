package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.cards.pcl.series.MadokaMagica.SayakaMiki;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class KyokoSakura_Ophelia extends PCLCard implements OnPurgeSubscriber
{
    public static final PCLCardData DATA = Register(KyokoSakura_Ophelia.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public KyokoSakura_Ophelia()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onPurge.Subscribe(this);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false).AddCallback(cards -> {
            PCLActions.Bottom.Draw(cards.size());
            int count = PCLJUtils.Count(cards, PCLGameUtilities::IsHindrance);
            if (count > 0) {
                PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(), secondaryValue * count);
            }
        });
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card.uuid.equals(this.uuid)) {
            PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
            PCLCombatStats.onPurge.Unsubscribe(this);
        }
    }
}
